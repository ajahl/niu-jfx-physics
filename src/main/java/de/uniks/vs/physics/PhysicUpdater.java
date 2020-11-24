package de.uniks.vs.physics;

import javafx.application.Platform;
import java.util.concurrent.CountDownLatch;

/**
 * Created by alex on 05/12/2016.
 */
public class PhysicUpdater extends Thread {
    private final Physics physics;

    public PhysicUpdater(Physics physics) {
        this.physics = physics;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(10);
                CountDownLatch latch = new CountDownLatch(1);
                Platform.runLater(() -> {
                    physics.update();
                    physics.remanence(0.003);

//                    if (physics.isDebugOn())
//                        physics.updateDebug();
                    latch.countDown();
                });
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
