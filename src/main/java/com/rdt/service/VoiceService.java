package com.rdt.service;

import org.springframework.web.multipart.MultipartFile;

public interface VoiceService {
    /**
     * Transcribes audio file to text using an AI model.
     * @param audioFile The recorded answer from the user.
     * @return The transcribed String.
     */
    String transcribe(MultipartFile audioFile);
}