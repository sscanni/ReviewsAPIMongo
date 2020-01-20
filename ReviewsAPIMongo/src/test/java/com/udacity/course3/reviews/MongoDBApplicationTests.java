package com.udacity.course3.reviews;

import static org.assertj.core.api.Assertions.assertThat;

import com.udacity.course3.reviews.entity.CommentDoc;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.util.SocketUtils;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

import java.util.ArrayList;
import java.util.List;

class MongoDBApplicationTests {
    private MongodExecutable mongodExecutable;
    private MongoTemplate mongoTemplate;

    @AfterEach
    void clean() {
        mongodExecutable.stop();
    }

    @BeforeEach
    void setup() throws Exception {
        String ip = "localhost";
        int randomPort = SocketUtils.findAvailableTcpPort();

        IMongodConfig mongodConfig = new MongodConfigBuilder().version(Version.Main.PRODUCTION)
                .net(new Net(ip, randomPort, Network.localhostIsIPv6()))
                .build();

        MongodStarter starter = MongodStarter.getDefaultInstance();
        mongodExecutable = starter.prepare(mongodConfig);
        mongodExecutable.start();
        mongoTemplate = new MongoTemplate(new MongoClient(ip, randomPort), "test");
    }

    @DisplayName("Given object When save object using MongoDB template Then object can be found")
    @Test
    void test() throws Exception {

        CommentDoc commentDoc  = new CommentDoc();
        commentDoc.setId(1);
        commentDoc.setComment("This is a test");
        List<CommentDoc> comDocList = new ArrayList();
        comDocList.add(commentDoc);

        // given
        DBObject objectToSave = BasicDBObjectBuilder.start()
                .add("reviewid", 1)
                .add("prodid", 2)
                .add("comments", comDocList)
                .get();

        // when
        mongoTemplate.save(objectToSave, "reviews");

        System.err.println(mongoTemplate.findAll(DBObject.class, "reviews").get(0));

        // then
        assertThat(mongoTemplate.findAll(DBObject.class, "reviews")).extracting("reviewid")
                .containsOnly(1);
        assertThat(mongoTemplate.findAll(DBObject.class, "reviews")).extracting("prodid")
                .containsOnly(2);
        assertThat(mongoTemplate.findAll(DBObject.class, "reviews")).extracting("comments")
                .asList();
    }
}

