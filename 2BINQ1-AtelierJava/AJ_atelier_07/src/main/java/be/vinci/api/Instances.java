package be.vinci.api;

import be.vinci.classes.User;
import be.vinci.instances.InstanceGraph1;
import be.vinci.services.ClassAnalyzer;
import be.vinci.services.InstancesAnalyzer;
import be.vinci.utils.InstanceGraphBuilder;
import jakarta.json.JsonStructure;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Send instances graph data to make object diagrams
 *
 * The instances graphs are initialized by a class containing the "initInstanceGraph" method,
 * building the instance graph, and returning it.
 *
 * The "instance builder class name" must be given and present into the "instances" package
 */
@Path("instances")
public class Instances {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonStructure getInstanceGraphInfo(@QueryParam("builderclassname") String builderClassname) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class aClass = null;    // TODO change this line to use the query parameter, and generate dynamically the builder
        try {
            aClass =  Class.forName("be.vinci.instances."+builderClassname);
        } catch (ClassNotFoundException e) {
            throw new WebApplicationException(404);
        }
/*        Object instanceOfaClass = aClass.getConstructor().newInstance(); // on construit une instance de aClass
        Object initInstanceGraph =  aClass.getMethod("initInstanceGraph").invoke(instanceOfaClass); // on lance la methode  initInstanceGraph
        InstancesAnalyzer analyzer = new InstancesAnalyzer(initInstanceGraph); // on appel

        return analyzer.getFullInfo();*/

        InstancesAnalyzer analyzer = null;
        for (Method m : aClass.getDeclaredMethods()){ // parcour tout les methode de aClass

            if(m.isAnnotationPresent(InstanceGraphBuilder.class)){ // trouve la methode avec l'annotation InstanceGraphBuilder
                analyzer = new InstancesAnalyzer(m.invoke(aClass.newInstance()));// on lance la methode
                return analyzer.getFullInfo(); // on renvoie l'affichage json
            }

        }

        return null;
    }
}
