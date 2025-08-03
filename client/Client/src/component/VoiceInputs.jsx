import React, { useState, useEffect, useRef } from 'react';
import axios from 'axios';

const VoiceInput = () => {
  const [transcript, setTranscript] = useState('');
  const [reply, setReply] = useState('');
  const [listening, setListening] = useState(false);
  const recognitionRef = useRef(null);

  useEffect(() => {
    const SpeechRecognition =
      window.SpeechRecognition || window.webkitSpeechRecognition;

    if (!SpeechRecognition) {
      alert('Speech recognition not supported in this browser.');
      return;
    }

    const recognition = new SpeechRecognition();
    recognition.lang = 'en-US';
    recognition.interimResults = false;
    recognition.continuous = false;

    recognition.onstart = () => setListening(true);

    recognition.onresult = async (event) => {
      const result = event.results[0][0].transcript;
      setTranscript(result);
      console.log('Transcript:', result);

      try {
        const response = await axios.post('/v0/voice/query', { userInput: result });
        const backendReply = response.data.reply || 'No response received';
        setReply(backendReply);
        speak(backendReply);
      } catch (error) {
        console.error('Error fetching backend response:', error);
        setReply('Error from backend');
      }
    };

    recognition.onerror = (event) => {
      console.error('Speech recognition error', event.error);
    };

    recognition.onend = () => {
      setListening(false);
    };

    recognitionRef.current = recognition;
  }, []);

  const startListening = () => {
    if (recognitionRef.current) {
      recognitionRef.current.start();
    }
  };

  const stopListening = () => {
    if (recognitionRef.current) {
      recognitionRef.current.stop();
    }
  };

  const speak = (text) => {
    const utterance = new SpeechSynthesisUtterance(text);
    window.speechSynthesis.speak(utterance);
  };

  return (
    <div className="p-4">
      <h2 className="text-xl font-bold mb-2">Bidirectional Voice Interface</h2>

      <button
        onClick={listening ? stopListening : startListening}
        className={`py-2 px-4 rounded text-white font-bold ${
          listening ? 'bg-red-500' : 'bg-green-500'
        }`}
      >
        {listening ? 'Stop Listening' : 'Start Listening'}
      </button>

      <div className="mt-4">
        <p><strong>Transcript:</strong> {transcript}</p>
        <p className="text-blue-600"><strong>Backend Reply:</strong> {reply}</p>
      </div>
    </div>
  );
};

export default VoiceInput;
