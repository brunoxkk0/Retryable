package dev.br.brunoxkk0.retryable.core;

public interface IRetryableTask extends Runnable{

    TaskState getState();

    void updateState(TaskState state);

    int getAttempt();

}
