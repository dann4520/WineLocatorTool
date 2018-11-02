package winelocator.dao;

import winelocator.App;
import winelocator.domain.Data;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class MongoDaoTest {

    @Autowired
    MongoDao mongoDao;

    @Test
    public void canWriteAndReadThings(){
        List<Data> dataList = new ArrayList<>();
        List<Data> result;
        dataList.add(new Data());
        dataList.add(new Data());
        mongoDao.setDb("winetest");
        mongoDao.insertData(dataList);
        result = mongoDao.getData();
        assertEquals("there are two documents", 2, result.size());
    }
}
