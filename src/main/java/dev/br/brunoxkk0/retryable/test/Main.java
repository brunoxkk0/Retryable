package dev.br.brunoxkk0.retryable.test;

import dev.br.brunoxkk0.retryable.core.RetryableTask;
import dev.br.brunoxkk0.retryable.core.RetryableTaskExecutor;
import dev.br.brunoxkk0.retryable.core.TaskState;

import java.util.Random;

public class Main {
    public static void main(String[] args) {

        RetryableTaskExecutor retryableTaskExecutor = new RetryableTaskExecutor();

        retryableTaskExecutor.queue(new RetryableTask() {

            @Override
            public void run() {

                System.out.println("Teste nº1");

                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                System.out.println("Teste nº1 ~~ FIM");

                if(new Random().nextInt(2) == 0){
                    updateState(TaskState.DONE);
                }

                requeueTask(retryableTaskExecutor);

            }

        });

        retryableTaskExecutor.queue(new RetryableTask() {

            @Override
            public void run() {

                System.out.println("Teste nº2");

                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                System.out.println("Teste nº2 ~~ FIM");

                if(new Random().nextInt(2) == 0){
                    updateState(TaskState.DONE);
                }

                requeueTask(retryableTaskExecutor);

            }

        });

        retryableTaskExecutor.queue(new RetryableTask() {

            @Override
            public void run() {

                System.out.println("Teste nº3");

                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                System.out.println("Teste nº3 ~~ FIM");

                if(new Random().nextInt(2) == 0){
                    updateState(TaskState.DONE);
                }

                requeueTask(retryableTaskExecutor);

            }

        });
    }
}
