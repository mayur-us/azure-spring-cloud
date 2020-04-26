package com.furkanisitan.core.data_access.hibernate;

import com.furkanisitan.core.Encryptor;
import com.furkanisitan.core.data_access.IEntityRepository;
import com.furkanisitan.core.entities.IEntity;
import com.furkanisitan.entities.Customer;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;

import org.jinq.orm.stream.JinqStream;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.util.List;
import java.util.Optional;

public class HibernateEntityRepositoryBase<T extends IEntity> implements IEntityRepository<T> {

	private Class<T> tClass;

	private RedisTemplate<String, Customer> redisTemplate ;
	
	private Jedis jedis;

	public HibernateEntityRepositoryBase(Class<T> tClass) {
		this.tClass = tClass;

		String cacheHostname = "EverestCache.redis.cache.windows.net";
		String cachekey = "OsWL7RTix6xrluZz4w0QNXQzrZt6KuzhQY0T+VwUerI=";
		boolean useSsl = true;
		// Connect to the Azure Cache for Redis over the SSL port using the key.
		JedisShardInfo shardInfo = new JedisShardInfo(cacheHostname, 6380,useSsl);
		shardInfo.setPassword(cachekey); /* Use your access key. */
		jedis = new Jedis(shardInfo);

		System.out.println("\nCache Command  : Ping");
		System.out.println("Cache Response : " + jedis.ping());

		JedisConnectionFactory factory = new JedisConnectionFactory(shardInfo);
//		factory.setHostName(redisHostName);
//		factory.setPort(redisPort);
//		factory.setUsePool(true);
//		factory.getPoolConfig().setMaxIdle(30);
//		factory.getPoolConfig().setMinIdle(10);
		
		redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(factory);
       
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.afterPropertiesSet();
	}

	@Override
	public List<T> getAll(JinqStream.Where<T, Exception> where) {
		EntityManager entityManager = HibernateHelper.getEntityManager();
		try {
			return HibernateHelper.JPA_STREAM_PROVIDER.streamAll(entityManager, tClass).where(where).toList();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			entityManager.close();
		}
		return null;
	}

	@Override
	public List<T> getAll() {
		EntityManager entityManager = HibernateHelper.getEntityManager();
		try {
			return HibernateHelper.JPA_STREAM_PROVIDER.streamAll(entityManager, tClass).toList();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			entityManager.close();
		}
		return null;
	}

//    @Override
//    public T getById(int id) {
//        EntityManager entityManager = HibernateHelper.getEntityManager();
//        try {
//            return entityManager.find(tClass, id);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            entityManager.close();
//        }
//        return null;
//    }

	@Override
	public T getById(int id) {
		String key = "CUSTOMER";
//      final ValueOperations<String, User> operations = redisTemplate.opsForValue();
		HashOperations hashOperations;
		hashOperations = redisTemplate.opsForHash();
		final boolean hasKey = redisTemplate.hasKey(key);
		T customer = null;
		if (hasKey) {
			customer = (T) hashOperations.get(key, id);
			if (null != customer) {
				System.out.println("UserService.findById() : fetching user from REDIS Cache >> " + ((Customer)customer).getEmail());
				return customer;
			}
		}
		EntityManager entityManager = HibernateHelper.getEntityManager();
		try {
			customer = entityManager.find(tClass, id);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			entityManager.close();
		}
//		final Optional<Customer> userOpt = Optional.ofNullable(userJPARepository.findById(id).get());
//		if (userOpt.isPresent()) {
//          operations.set(key, post.get(), 10, TimeUnit.SECONDS);
//			customer = userOpt.get();
			Customer c = (Customer)customer;
			hashOperations.put("CUSTOMER", c.getId(), c);
			System.out.println("UserService.findById() : Inserting value from DB REDIS Cache >> " + c.toString());
//		}
		return customer;
	}

	@Override
	public T getByEmail(String email) {
		EntityManager entityManager = HibernateHelper.getEntityManager();
		Encryptor encryptor = Encryptor.getCryptoInstance();
		email = encryptor.base64Encode(email);

		Query q = entityManager.createNativeQuery(
				"SELECT a.id,a.salary,a.fullname,a.email,a.phoneNumber,a.dateOfBirth FROM Customers a WHERE a.email = ?",
				Customer.class);
		q.setParameter(1, email);
		List<Customer> customers = q.getResultList();

		try {
			return (T) customers.get(0);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			entityManager.close();
		}
		return null;
	}

	@Override
	public T get(JinqStream.Where<T, Exception> where) {
		EntityManager entityManager = HibernateHelper.getEntityManager();
		try {
			return HibernateHelper.JPA_STREAM_PROVIDER.streamAll(entityManager, tClass).where(where).findFirst().get();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			entityManager.close();
		}
		return null;
	}

	@Override
	public boolean add(T entity) {
		EntityManager entityManager = HibernateHelper.getEntityManager();
		try {
			entityManager.getTransaction().begin();
			entityManager.persist(entity);
			entityManager.getTransaction().commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			entityManager.close();
		}
	}

	@Override
	public boolean update(T entity) {
		EntityManager entityManager = HibernateHelper.getEntityManager();
		try {
			entityManager.getTransaction().begin();
			entityManager.merge(entity);
			entityManager.getTransaction().commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			entityManager.close();
		}
	}

	@Override
	public boolean delete(T entity) {
		EntityManager entityManager = HibernateHelper.getEntityManager();
		try {
			entityManager.getTransaction().begin();
			entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
			entityManager.getTransaction().commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			entityManager.close();
		}
	}
}
