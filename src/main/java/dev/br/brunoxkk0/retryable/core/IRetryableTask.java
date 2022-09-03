package dev.br.brunoxkk0.retryable.core;

public interface IRetryableTask<T> extends Runnable{

    TaskState getState();

    void updateState(TaskState state);

    int getAttempt();

}
