package ru.vsu.savina.graphvisualization.exception;

public class FileReaderException extends RuntimeException{
    private final String message;

    public FileReaderException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
