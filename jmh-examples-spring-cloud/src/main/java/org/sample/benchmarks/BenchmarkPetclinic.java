package org.sample.benchmarks;

import org.openjdk.jmh.annotations.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.samples.petclinic.PetClinicApplication;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.PetController;
import java.io.File;
import java.io.IOException;

@SpringBootApplication(scanBasePackages = "org.sample.billing.*")
public class BenchmarkBilling {

    @State(Scope.Thread)
    public static class StateVariables {
        public PetController petcontroller;

        @Setup(Level.Trial)
        public void initMysql() throws IOException {
            System.out.println("Init mysql");
            //docker run -e MYSQL_ROOT_PASSWORD=petclinic -e MYSQL_DATABASE=petclinic -p 3306:3306 mysql:5.7.8
            ProcessBuilder pb = new ProcessBuilder("docker","run","-e",
                    "MYSQL_ROOT_PASSWORD=petclinic", "-e",  "MYSQL_DATABASE" +
                    "=petclinic", "-p", "3306:3306", "mysql:5.7.8");
            pb.directory(new File("."));
            Process p = pb.start();
        }
    }

    public static class StateVariablesJaeger extends StateVariables {
        @Setup(Level.Iteration)
        public void doSetup() {

            System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "jaegerTracer");

            ApplicationContext c = SpringApplication.run(PetClinicApplication.class);
            petcontroller =  c.getBean(PetController.class);
        }
    }

    public static class StateVariablesNoopTracer extends StateVariables {
        @Setup(Level.Iteration)
        public void doSetup() {

            System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "noopTracer");

            ApplicationContext c = SpringApplication.run(PetClinicApplication.class);
            petcontroller =  c.getBean(PetController.class);
        }
    }

    public static class StateVariablesHaystack extends StateVariables {
        @Setup(Level.Iteration)
        public void doSetup() {

            System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "haystackTracer");

            ApplicationContext c = SpringApplication.run(PetClinicApplication.class);
            petcontroller =  c.getBean(PetController.class);
        }
    }

    public Owner findPetById(StateVariables state){
        return state.petcontroller.findOwner(1);
    }
}


