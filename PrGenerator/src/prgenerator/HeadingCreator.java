package prgenerator;

/**
 * Objects of this class are capable to create headings.
 *
 * @author Jörg Woditschka The method "createHeading()" is called to start the
 * creation process.
 */
public class HeadingCreator {

    /**
     * This method creates a heading and stores hands it to the database. One
     * template is randomly picked and filled with filling words. In the end the
     * complete heading can be passed to the database to store it.
     */
    public void createHeading() {
        String[] templateFill = PrGenerator.mainDatabase.getTemplateFill(); //gets fillings for the template out of the database
        String path = "data/templates_heading.xml";
        String heading = "";
        Template templateReader = new Template();

        //picking a template and simply replacing the place holders by the filling words.
        if (templateFill[0] != null) {
            heading += templateReader.readXML(path, "template", "templates"); //a random template is picked by this call
            heading = heading.replace("_location_", templateFill[0]);
        }
        if (templateFill[1] != null) {
            heading = heading.replace("_date_", templateFill[1]);
        }
        if (templateFill[2] != null) {
            heading = heading.replace("_keyAspect_", templateFill[2]);
        }

        PrGenerator.mainDatabase.setCreatedHeading(heading); //handing the heading to the database
    }
}