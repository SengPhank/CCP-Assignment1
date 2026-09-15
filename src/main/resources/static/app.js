let audioRecorder;    // Store MediaRecorder manager
let mediaStream;      // Keep reference to stop mic hardware tracks
let chunks = [];      // Store audio temporary
let blob = null;      // Store final audio

// Define buttons
let recordBtn = document.getElementById("record-btn");
let stpCnclBtn = document.getElementById("stop-cancel-btn"); // stop and cancel 
let finishBtn = document.getElementById("finish-btn");
let audioHearing = document.getElementById("audio-hearing");
let sendBtn = document.getElementById("send-btn");

let inputText = document.getElementById("url-input");
let testBtn = document.getElementById("test-btn");

// Define other ids
let outText = document.getElementById("out-text");

// Define variables
let paused = false;
let recording = false;

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
            audioHearing.src = audioUrl;
        }

        recording = false;
        paused = false;
        recordBtn.textContent = "Start";
        recordBtn.disabled = false;
        stpCnclBtn.textContent = "Pause";
        stpCnclBtn.disabled = true;
    };

    // Start recording
    audioRecorder.start();
    paused = false;
    recording = true;

    // Update buttons and variables
    recordBtn.textContent = "Resume";
    stpCnclBtn.textContent = "Pause";

    recordBtn.disabled = true;
    stpCnclBtn.disabled = false;
    finishBtn.disabled = true;
}

function startResumeRecording() {
    // Not currently recording, start now
    if (!recording) {
        startRecording();
    // Already recording, clicked to resume
    } else if (paused) {
        audioRecorder.resume();
        paused = false;
		
		finishBtn.disabled = true;
        recordBtn.disabled = true;
        stpCnclBtn.textContent = "Pause";
    }
}

function pauseCancelRecording() {
    if (!audioRecorder || !recording) return;

    // Not paused, pause the recording now
    if (!paused) {
        audioRecorder.pause();
        paused = true;

        stpCnclBtn.textContent = "Cancel";
        recordBtn.disabled = false; 
		finishBtn.disabled = false;
    // Already paused, cancel the recording
    } else {
        // Reset state and discard
        chunks = [];
        blob = null;
        if (audioHearing) audioHearing.src = "";

        if (audioRecorder.state !== "inactive") {
            audioRecorder.stop();
        }

        finishBtn.disabled = true;
    }
}

// Stop audio capture and trigger onstop to build the blob
function finishRecording() {
    if (audioRecorder && audioRecorder.state !== "inactive") {
        audioRecorder.stop();
    }
	finishBtn.disabled = true;
}

async function sendAudioToServer() {
    if (!blob) {
        console.error("No audio found");
        return;
    }

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



// Temp function
function updateOutText() {
	let randomText = "Out: ";
	
	// Random number between x and y
	const x = 40; const y = 200;
	const textLen = Math.floor(Math.random() * (y - x + 1)) + 32;
	
	for (let i = 0; i < textLen; i++) {
		// random letter
		const alphaBet = Math.floor(Math.random() * (97+ - 65 + 1)) + 32;
		randomText += String.fromCharCode(alphaBet);
	}
	
	outText.textContent = randomText;
}

// Test function
async function sendAPICall() {
	// Get custom api path from textbox
	let url = inputText.value;
	// inputText.value="";
	console.log(`attmpting send ${url}`);
	
	// Send a request to the api at "url"
	try {
		const resp = await fetch(url, {method : "POST"});
		if (!resp.ok) {
			throw new Error(`Resp Status: ${resp.status}`);
		}
		const res = await resp.json();
		console.log(`API said ${res}`);
		
	} catch (err) {
		console.error(`Failed because ${err}`);
	}
}

recordBtn.addEventListener("click", startResumeRecording);
stpCnclBtn.addEventListener("click", pauseCancelRecording);
finishBtn.addEventListener("click", finishRecording);
// sendBtn.addEventListener("click", updateOutText); // temporary feature
sendBtn.addEventListener("click", sendAudioToServer);

testBtn.addEventListener("click", sendAPICall);

