package com.example.jaxbproblem;

import com.cluetrust.xml.gpxdata._1._0.LapType;
import com.cluetrust.xml.gpxdata._1._0.TriggerType;
import com.topografix.gpx._1._1.*;
import org.junit.Test;

import javax.xml.bind.*;
import java.io.File;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by ron on 25-4-15.
 */
public class ProblemTest {

    @Test
    public void testUnMarshalling() {
        try {
//            JAXBContext jaxbContext = JAXBContext.newInstance("com.topografix.gpx._1._1:com.cluetrust.xml.gpxdata._1._0");
            JAXBContext jaxbContext = JAXBContext.newInstance(GpxType.class, com.cluetrust.xml.gpxdata._1._0.ObjectFactory.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            File file = new File("src/test/resources/gpx-example.gpx");
            JAXBElement<GpxType> unmarshal = (JAXBElement<GpxType>) unmarshaller.unmarshal(file);
            GpxType gpx = unmarshal.getValue();
            ExtensionsType extensions = gpx.getExtensions();
            List<Object> extensionsAny = extensions.getAny();
            for (Object object : extensionsAny){
                System.out.println(object.getClass());
                if (object instanceof JAXBElement) {
                    JAXBElement element = (JAXBElement) object;
                    LapType lapType = (LapType) element.getValue();
                    System.out.println(lapType.getStartTime());

                }
            }

            assertTrue(file.canRead());
            System.out.printf(file.getAbsolutePath());
        } catch (PropertyException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMarshalling() {
        GpxType gpx = new GpxType();
        gpx.setCreator("test");
        gpx.setVersion("1.0");
        TrkType track = makeTrack();
    LapType lapType = new LapType();
        ExtensionsType extensionsType = new ExtensionsType();
        extensionsType.getAny().add(lapType);
    gpx.setExtensions(extensionsType);

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance("com.topografix.gpx._1._1:com.cluetrust.xml.gpxdata._1._0");
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(new ObjectFactory().createGpx(gpx), System.out);
        } catch (PropertyException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    private TrkType makeTrack() {
        TrkType track = new TrkType();
        TrksegType segment = new TrksegType();
        track.getTrkseg().add(segment);
        WptType waypoint = new WptType();
        waypoint.setLat(new BigDecimal(51.63126637));
        waypoint.setLon(new BigDecimal(5.236223));
        return track;
    }
}
