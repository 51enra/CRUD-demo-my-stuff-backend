package de.telekom.sea.mystuff.backend;

import java.time.LocalDate;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class DevBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private ItemRepo repository;

    public DevBootstrap(ItemRepo repository) {
        this.repository = repository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent arg0) {
        this.initData();
    }
    
/* Copy-and-paste for POST test:
{
  "amount": 12,
  "description": "Kiste mit ca. 100 Platten",
  "lastUsed": "2010-12-12",
  "location": "Raum 3",
  "name": "Schallplatten"
}
*/

    private void initData() {
   
        // Bücherkiste
        Item testItem1 = new Item();
        testItem1.setName("Bücherkiste");
        testItem1.setAmount(5);
        testItem1.setLocation("Raum 2");
        testItem1.setDescription("Omas Bücher");
        testItem1.setLastUsed(LocalDate.parse("2019-01-01")); // oder bei Java Date: Date.valueOf()
        this.repository.save(testItem1);
        
        // Fahrrad
        Item testItem2 = new Item();
        testItem2.setName("Fahrrad");
        testItem2.setAmount(1);
        testItem2.setLocation("Raum 1");
        testItem2.setDescription("Kinderfahrrad");
        testItem2.setLastUsed(LocalDate.parse("2017-03-06"));
        this.repository.save(testItem2);
        
        // Einmachgläser
        Item testItem3 = new Item();
        testItem3.setName("Einmachgläser");
        testItem3.setAmount(27);
        testItem3.setLocation("Raum 1");
        testItem3.setDescription("Weckgläser mit Dichtgummi");
        testItem3.setLastUsed(LocalDate.parse("2020-02-02"));
        this.repository.save(testItem3);  

    }

}

