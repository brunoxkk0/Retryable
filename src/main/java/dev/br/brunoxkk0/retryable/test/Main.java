package dev.br.brunoxkk0.retryable.test;

import dev.br.brunoxkk0.retryable.builder.RetryableTaskBuilder;
import dev.br.brunoxkk0.retryable.core.RetryableTask;
import dev.br.brunoxkk0.retryable.core.RetryableTaskExecutor;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


public class Main {

    static ThreadPoolExecutor poolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10, r -> {
        Thread thread = new Thread(r);
        thread.setDaemon(false);
        thread.setName("RetryableTask");
        return thread;
    });

    public static void main(String[] args) {

        RetryableTaskExecutor retryableTaskExecutor = new RetryableTaskExecutor(5, poolExecutor);

        RetryableTask<String> s = RetryableTaskBuilder.<String>newBuilder().named("Teste 1").of((task) -> {

            Thread.sleep(1000);

            if(task.getAttempt() != 6){
                throw new Exception("Deu erro hein" + task.getName());
            }

            return "Thread " + task.getName() + " Passou";
        }).done((taskReturn) -> {
            System.out.println("Retorno do objeto " + taskReturn.task().getName() + ": " + taskReturn.returnValue());
        }).error((e) -> {
            System.out.println("Error: " + e.error().getLocalizedMessage() + " Task: " + e.task().getName());
        }).build();

        retryableTaskExecutor.queue(s);

        RetryableTaskBuilder.<String>newBuilder().named("Teste 2").of((task) -> {
            Thread.sleep(1000);

            if(task.getAttempt() != 2){
                throw new Exception("Deu erro hein" + task.getName());
            }

            return "Thread " + task.getName() + " Passou";
        }).done((taskReturn) -> {
            System.out.println("Retorno do objeto " + taskReturn.task().getName() + ": " + taskReturn.returnValue());
        }).error((e) -> {
            System.out.println("Error: " + e.error().getLocalizedMessage() + " Task: " + e.task().getName());
        }).buildAndQueue(retryableTaskExecutor);
    }
}
