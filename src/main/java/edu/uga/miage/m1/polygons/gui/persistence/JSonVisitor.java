package edu.uga.miage.m1.polygons.gui.persistence;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import edu.uga.miage.m1.polygons.gui.shapes.SimpleShape;

/**
 * @author <a href="mailto:christophe.saint-marcel@univ-grenoble-alpes.fr">Christophe</a>
 */
public class JSonVisitor implements Visitor {

    private JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();

    @Override
    public void visit(SimpleShape simpleShape) {
        jsonObjectBuilder = Json.createObjectBuilder()
                .add("type", simpleShape.getType())
                .add("x", simpleShape.getX())
                .add("y", simpleShape.getY());
    }


    public String getRepresentation() {
        JsonObject jsonObject = jsonObjectBuilder.build();
        return jsonObject.toString();
    }

    public void reset() {
        jsonObjectBuilder = Json.createObjectBuilder();
    }
}
