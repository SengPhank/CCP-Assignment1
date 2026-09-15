let audioRecorder;    // Store MediaRecorder manager
let mediaStream;      // Keep reference to stop mic hardware tracks
let chunks = [];      // Store audio temporary
let blob = null;      // Store final audio

// Define buttons
let recordBtn = document.getElementById("record-btn");
let stopBtn = document.getElementById("stop-btn"); 
let cancelBtn = document.getElementById("cancel-btn");
let audioHearing = document.getElementById("audio-hearing");

// Define other ids
let outText = document.getElementById("out-text");

async function startRecording() {
    chunks = [];
    blob = null;

    try {
        mediaStream = await navigator.mediaDevices.getUserMedia({ audio: true });
    } catch (err) {
        console.error(`Error starting recording: ${err}`);
        return;
    }

    // Determine supported mimeType safely
    let mimeType = "audio/webm; codecs=opus";
    if (!MediaRecorder.isTypeSupported(mimeType)) {
        mimeType = "audio/webm";
    }

    audioRecorder = new MediaRecorder(mediaStream, { mimeType });

    // Store all audio data into chunks when available
    audioRecorder.ondataavailable = event => {
        if (event.data && event.data.size > 0) {
            chunks.push(event.data);
        }
    };

    // When media is stopped fully
    audioRecorder.onstop = () => {
        // Stop hardware microphone access
        if (mediaStream) {
            mediaStream.getTracks().forEach(track => track.stop());
        }

        // Save into final audio blob if chunks exist
        if (chunks.length > 0) {
            blob = new Blob(chunks, { type: mimeType });
            const audioUrl = URL.createObjectURL(blob);
			if (audioHearing) {
                audioHearing.src = audioUrl;
            }
        }

        recordBtn.disabled = false;
        stopBtn.disabled = true;
		stopBtn.disabled = true;
		
		// Send audio to server IMMEDIATELY after "stop" is ran. 
		sendAudioToServer();
    };

    // Start recording
    audioRecorder.start();

    // Update buttons
    recordBtn.disabled = true;
    stopBtn.disabled = false;
	cancelBtn.disabled = true;
}

// Pause recording
function stopRecording() {
    if (audioRecorder && audioRecorder.state !== "inactive") {
        audioRecorder.stop();
    }
    stopBtn.disabled = true;
}

function clearRecording() {
	// Discard and reset audio state
    chunks = [];
    blob = null;
	
	// Audio hearing src
    if (audioHearing) audioHearing.src = "";
	
	// Stop the audio recording if there is an audiorecorder
    if (audioRecorder && audioRecorder.state !== "inactive") {
        audioRecorder.stop();
    }

    recordBtn.disabled = false;
    stopBtn.disabled = true;
    cancelBtn.disabled = true;
}

async function sendAudioToServer() {
	// must stop audio recorder before continuing
	if (!audioRecorder && audioRecorder.state === "recording") return;
	
	outText.textContent = "Getting audio to backend...";
    if (!blob) {
        console.error("No audio found");
        return;
    }
	outText.textContent = "Sending audio to backend...";

    // Pack the blob into multipart form data
    const formData = new FormData();
    formData.append("file", blob, "recording.webm");

    try {
        // Send audio to java backend via POST
        const resp = await fetch("/api/v1/audio", {
            method: "POST",
            body: formData
        });

        if (!resp.ok) throw new Error(`Server returned status: ${resp.status}`);

        const data = await resp.json();
        console.log("Transcription result:", data);
        outText.textContent = data.message;
		
    } catch (err) {
        console.error("Failed to upload audio because:", err);
		outText.textContent = "Transcribed message: " + err;
    }
}

recordBtn.addEventListener("click", startRecording);
stopBtn.addEventListener("click", stopRecording);
cancelBtn.addEventListener("click", clearRecording);

// TEST/DEBUG FEATURES =====================================================================================
let inputText = document.getElementById("api-url-input");
let postBtn = document.getElementById("api-post-btn");
let getBtn = document.getElementById("api-get-btn");
let genTrans = document.getElementById("random-transcribe");

// Temp function
function updateOutText() {
	let randomText = "";
	
	// Random string of length between x and y
	const x = 500; const y = 10000;
	const textLen = Math.floor(Math.random() * (y - x + 1)) + 32;
	
	for (let i = 0; i < textLen; i++) {
		// random letter
		const alphaBet = Math.floor(Math.random() * (97+ - 65 + 1)) + 32;
		randomText += String.fromCharCode(alphaBet);
	}
	
	outText.textContent = randomText;
}

// Test function
async function sendAPICall(meth) {
	if (meth !== "POST") meth = "GET";
	// Get custom api path from textbox
	let url = inputText.value;
	console.log(`attmpting send ${url}`);
	
	// Send a request to the api at "url"
	try {
		const resp = await fetch(url, {method : meth});
		if (!resp.ok) {
			throw new Error(`Resp Status: ${resp.status}`);
		}
		const res = await resp.json();
		console.log(`API said ${res}`);
		
	} catch (err) {
		console.error(`Failed because ${err}`);
	}
}

postBtn.addEventListener("click", function(){sendAPICall("POST")});
getBtn.addEventListener("click", function(){sendAPICall("GET")});
genTrans.addEventListener("click", updateOutText);
