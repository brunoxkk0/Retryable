package dev.br.brunoxkk0.retryable.core;

public enum TaskState {
    WAITING,
    QUEUED,
    PROCESSING,
    DONE,
    REPROCESSING,
    ERROR
}
