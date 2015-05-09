package com.assertramdom.poc.batch.processor;

import com.assertramdom.poc.batch.domain.Furniture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

/**
 * Created on 09/05/15.
 */
public class FurnitureItemProcessor  implements ItemProcessor<Furniture, Furniture>{

    private static Logger logger = LoggerFactory.getLogger(FurnitureItemProcessor.class);

    @Override
    public Furniture process(Furniture furniture) throws Exception {

        Furniture improvedFurniture = new Furniture();
        improvedFurniture.setName(furniture.getName());
        improvedFurniture.setCost(furniture.getCost() + 100);
        logger.debug("Improved cost with same quality ;) ");
        return improvedFurniture;
    }
}
