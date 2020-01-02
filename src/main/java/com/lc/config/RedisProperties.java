package com.lc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.jmx.support.RegistrationPolicy;

import java.time.Duration;

@Configuration
@ComponentScan("com.lc")
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
@PropertySource("classpath:redis.properties")
public class RedisProperties {
    @Value("${spring.redis.database}")
    private int database;
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.ssl}")
    private boolean ssl;
    @Value("${spring.redis.timeout}")
    private Duration timeout;



//    @Value("sentinel")
//    private RedisProperties.Sentinel sentinel;
//    @Value("cluster")
//    private RedisProperties.Cluster cluster;
//    private final RedisProperties.Jedis jedis = new RedisProperties.Jedis();
//    private final RedisProperties.Lettuce lettuce = new RedisProperties.Lettuce();

    public RedisProperties() {
    }

    @Bean(name="lettuceConnectionFactoryPrivate")
    public LettuceConnectionFactory setLettuceConnectionFactory(){
        RedisStandaloneConfiguration standaloneConfig = new RedisStandaloneConfiguration(host,port);
        standaloneConfig.setDatabase(database);
        standaloneConfig.setPassword(RedisPassword.of(password));

        LettuceClientConfiguration lettuceClientConfiguration =
                LettuceClientConfiguration.builder()
                        .shutdownTimeout(timeout)
                        .build();
        return new LettuceConnectionFactory(standaloneConfig,lettuceClientConfiguration);
    }


   /* public static class Lettuce {
        private Duration shutdownTimeout = Duration.ofMillis(100L);
        private RedisProperties.Pool pool;

        public Lettuce() {
        }

        public Duration getShutdownTimeout() {
            return this.shutdownTimeout;
        }

        public void setShutdownTimeout(Duration shutdownTimeout) {
            this.shutdownTimeout = shutdownTimeout;
        }

        public RedisProperties.Pool getPool() {
            return this.pool;
        }

        public void setPool(RedisProperties.Pool pool) {
            this.pool = pool;
        }
    }*/

//    public static class Jedis {
//        private RedisProperties.Pool pool;
//
//        public Jedis() {
//        }
//
//        public RedisProperties.Pool getPool() {
//            return this.pool;
//        }
//
//        public void setPool(RedisProperties.Pool pool) {
//            this.pool = pool;
//        }
//    }

   /* public static class Sentinel {
        private String master;
        private List<String> nodes;

        public Sentinel() {
        }

        public String getMaster() {
            return this.master;
        }

        public void setMaster(String master) {
            this.master = master;
        }

        public List<String> getNodes() {
            return this.nodes;
        }

        public void setNodes(List<String> nodes) {
            this.nodes = nodes;
        }
    }*/

   /* public static class Cluster {
        private List<String> nodes;
        private Integer maxRedirects;

        public Cluster() {
        }

        public List<String> getNodes() {
            return this.nodes;
        }

        public void setNodes(List<String> nodes) {
            this.nodes = nodes;
        }

        public Integer getMaxRedirects() {
            return this.maxRedirects;
        }

        public void setMaxRedirects(Integer maxRedirects) {
            this.maxRedirects = maxRedirects;
        }
    }*/

    /*public static class Pool {
        private int maxIdle = 8;
        private int minIdle = 0;
        private int maxActive = 8;
        private Duration maxWait = Duration.ofMillis(-1L);

        public Pool() {
        }

        public int getMaxIdle() {
            return this.maxIdle;
        }

        public void setMaxIdle(int maxIdle) {
            this.maxIdle = maxIdle;
        }

        public int getMinIdle() {
            return this.minIdle;
        }

        public void setMinIdle(int minIdle) {
            this.minIdle = minIdle;
        }

        public int getMaxActive() {
            return this.maxActive;
        }

        public void setMaxActive(int maxActive) {
            this.maxActive = maxActive;
        }

        public Duration getMaxWait() {
            return this.maxWait;
        }

        public void setMaxWait(Duration maxWait) {
            this.maxWait = maxWait;
        }
    }*/
}
