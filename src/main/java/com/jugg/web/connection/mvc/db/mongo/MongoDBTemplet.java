package com.jugg.web.connection.mvc.db.mongo;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jugg.web.connection.mvc.entity.Db2Connection;
import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

/**
 * 
 * @author JingWangZou
 *
 */
public class MongoDBTemplet {
	
	private static Logger logger = LoggerFactory.getLogger(MongoDBTemplet.class);

	/**
	 * MongoClient的实例代表数据库连接池，是线程安全的，可以被多线程共享，客户端在多线程条件下仅维持一个实例即可
	 * Mongo是非线程安全的，目前mongodb API中已经建议用MongoClient替代Mongo
	 */
	private MongoClient mongoClient = null;

	/**
	 * 构造方法
	 *
	 * @param ip   127.0.0.1
	 * @param port 27107
	 */
	private MongoDBTemplet(String ip, int port) {
		if (mongoClient == null) {
			MongoClientOptions.Builder build = new MongoClientOptions.Builder();
			/*
			 * 一个线程访问数据库的时候，在成功获取到一个可用数据库连接之前的最长等待时间为2分钟
			 * 这里比较危险，如果超过maxWaitTime都没有获取到这个连接的话，该线程就会抛出Exception
			 * 故这里设置的maxWaitTime应该足够大，以免由于排队线程过多造成的数据库访问失败
			 */
			//创建个 credential对象
//	        MongoCredential credential = MongoCredential.createCredential("username", "databaseName", "password".toCharArray());
	        //把对象传入mongoClient的构造方法中，说明：用户名密码端口啥的都正确会得到mongoClient,否则程序会报错
	        //mongodb还有很多种验证方式 详见：http://mongodb.github.io/mongo-java-driver/3.0/driver/reference/connecting/authenticating/ 
	       
			build.maxWaitTime(1000 * 60 * 2);
			build.connectTimeout(1000 * 60 * 1); // 与数据库建立连接的timeout设置为1分钟
			build.socketTimeout(0);// 套接字超时时间，0无限制
			build.connectionsPerHost(300); // 连接池设置为300个连接,默认为100
			build.threadsAllowedToBlockForConnectionMultiplier(5000);// 线程队列数，如果连接线程排满了队列就会抛出“Out of semaphores to get
			
			build.writeConcern(WriteConcern.ACKNOWLEDGED);

			MongoClientOptions myOptions = build.build();
			try {
				// 数据库连接实例
//				mongoClient = new MongoClient(new ServerAddress(ip, port), Arrays.asList(credential), myOptions);
				mongoClient = new MongoClient(new ServerAddress(ip, port), myOptions);
				logger.error("MongoDb-init|info| the mongo client init is successful...");
			} catch (MongoException e) {
				logger.error("MongoDb-init|error| details ", e);
			}

		}
	}

	private static MongoDBTemplet mongoDBTemplet = null;

	/**
	 * 获取实例
	 *
	 * @param ip   127.0.0.1
	 * @param port 27107
	 * @return
	 */
	public synchronized static MongoDBTemplet getInstance(String ip, int port) {
		if (mongoDBTemplet == null) {
			mongoDBTemplet = new MongoDBTemplet(ip, port);
		}
		return mongoDBTemplet;
	}

	public boolean isExits(String dbName, String collectionName, Map<String, Object> filterMap) {
		if (filterMap != null) {
			FindIterable<Document> docs = mongoClient.getDatabase(dbName).getCollection(collectionName)
					.find(new Document(filterMap));

			Document doc = docs.first();
			if (doc != null) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public boolean insert(String dbName, String collectionName, Map<String, Object> object) {
		if (object != null) {
			mongoClient.getDatabase(dbName).getCollection(collectionName).insertOne(new Document(object));
			return true;
		}
		return false;
	}

	public boolean deleteById(String dbName, String collectionName, String _id) {
		ObjectId objectId = new ObjectId(_id);
		Bson filter = Filters.eq("_id", objectId);

		DeleteResult deleteResult = getDatabase(dbName).getCollection(collectionName).deleteOne(filter);
		long deletedCount = deleteResult.getDeletedCount();

		return deletedCount > 0 ? true : false;
	}

	public boolean delete(String dbName, String collectionName, Map<String, Object> map) {
		if (map != null) {
			DeleteResult result = mongoClient.getDatabase(dbName).getCollection(collectionName)
					.deleteMany(new Document(map));
			long deletedCount = result.getDeletedCount();
			return deletedCount > 0 ? true : false;
		}
		return false;
	}

	public boolean updateOne(String dbName, String collectionName, Map<String, Object> filterMap,
			Map<String, Object> updateMap) {
		if (filterMap != null && filterMap.size() > 0 && updateMap != null) {
			UpdateResult result = mongoClient.getDatabase(dbName).getCollection(collectionName)
					.updateOne(new Document(filterMap), new Document("$set", new Document(updateMap)));
			long modifiedCount = result.getModifiedCount();
			return modifiedCount > 0 ? true : false;
		}

		return false;
	}

	public boolean updateById(String dbName, String collectionName, String _id, Document updateDoc) {
		ObjectId objectId = new ObjectId(_id);
		Bson filter = Filters.eq("_id", objectId);

		UpdateResult result = getDatabase(dbName).getCollection(collectionName).updateOne(filter,
				new Document("$set", updateDoc));
		long modifiedCount = result.getModifiedCount();

		return modifiedCount > 0 ? true : false;
	}

	public List<Document> find(String dbName, String collectionName, Bson filter) {
		List<Document> resultList = new ArrayList<Document>();
		if (filter != null) {
			FindIterable<Document> docs = mongoClient.getDatabase(dbName).getCollection(collectionName).find(filter);
			docs.forEach(new Block<Document>() {

				public void apply(Document document) {
					resultList.add(document);
				}
			});
		}

		return resultList;
	}

	public Document findById(String dbName, String collectionName, String _id) {
		ObjectId objectId = new ObjectId(_id);

		Document doc = getDatabase(dbName).getCollection(collectionName).find(Filters.eq("_id", objectId)).first();
		return doc;
	}
	
	/** document cast entity @param <T>**/
	@SuppressWarnings("unchecked")
	public <T> List<T> find(String dbName, String collectionName, Bson filter, Class<?> cals) {
		List<Document> docList = new ArrayList<Document>();
		if (filter != null) {
			FindIterable<Document> docs = mongoClient.getDatabase(dbName).getCollection(collectionName).find(filter);
			docs.forEach(new Block<Document>() {

				public void apply(Document document) {
					docList.add(document);
				}
			});
		}
		
		List<T> tlist = new ArrayList<T>();
		try {
			for(Document doc: docList) {
				tlist.add((T) toBean(doc, cals));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return tlist;
	}

	@SuppressWarnings("unchecked")
	public <T> T findById(String dbName, String collectionName, String _id, Class<?> cals) {
		
		try {
			ObjectId objectId = new ObjectId(_id);
			Document doc = getDatabase(dbName).getCollection(collectionName).find(Filters.eq("_id", objectId)).first();
			
			if(null == doc) {
				logger.warn("no serach mongo document by id = " + _id);
				return null;
			}
			return (T) toBean(doc, cals);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	

	/**
	 * 分页查询
	 *
	 * @param dbName
	 * @param collectionName
	 * @param filter
	 * @param pageIndex      从1开始
	 * @param pageSize
	 * @return
	 */
	public List<Document> findByPage(String dbName, String collectionName, Bson filter, int pageIndex, int pageSize) {
		Bson orderBy = new BasicDBObject("_id", 1);

		List<Document> resultList = new ArrayList<Document>();
		FindIterable<Document> docs = getDatabase(dbName).getCollection(collectionName).find(filter).sort(orderBy)
				.skip((pageIndex - 1) * pageSize).limit(pageSize);
		docs.forEach(new Block<Document>() {
			public void apply(Document document) {
				resultList.add(document);
			}
		});

		return resultList;
	}

	public MongoCollection<Document> getCollection(String dbName, String collectionName) {
		return mongoClient.getDatabase(dbName).getCollection(collectionName);
	}

	public MongoDatabase getDatabase(String dbName) {
		return mongoClient.getDatabase(dbName);
	}

	public long getCount(String dbName, String collectionName) {
		return getDatabase(dbName).getCollection(collectionName).count();
	}

	/**
	 * 查询dbName下的所有表名
	 *
	 * @param dbName
	 * @return
	 */
	public List<String> getAllCollections(String dbName) {
		MongoIterable<String> cols = getDatabase(dbName).listCollectionNames();
		List<String> _list = new ArrayList<String>();
		for (String s : cols) {
			_list.add(s);
		}
		return _list;
	}

	/**
	 * 获取所有数据库名称列表
	 *
	 * @return
	 */
	public MongoIterable<String> getAllDatabaseName() {
		MongoIterable<String> s = mongoClient.listDatabaseNames();
		return s;
	}

	/**
	 * 删除一个数据库
	 *
	 * @param dbName
	 */
	public void dropDatabase(String dbName) {
		getDatabase(dbName).drop();
	}

	/**
	 * 删除collection
	 *
	 * @param dbName
	 * @param collectionName
	 */
	public void dropCollection(String dbName, String collectionName) {
		getDatabase(dbName).getCollection(collectionName).drop();
	}

	public void close() {
		if (mongoClient != null) {
			mongoClient.close();
			mongoClient = null;
		}
	}
	
	
	/**
	 * document 封装到 entity
	 * @param doc
	 * @param cals
	 * @return
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IntrospectionException
	 * @throws InstantiationException
	 */
	public static Object toBean(Document doc, Class<?> cals) throws NoSuchMethodException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, SecurityException, NoSuchFieldException,IntrospectionException,InstantiationException {
		
		Object object = cals.newInstance();
		Field[] fields = cals.getDeclaredFields();//获取属性名
        
        for(Field field: fields) {
        	
        	//返回的是一个参数类型
        	String type = field.getGenericType().toString();
        	 //返回的是一个类对象 
//          Class classType = field.getType();
            PropertyDescriptor pd = new PropertyDescriptor(field.getName(), cals);
            //获取某个属性的set方法
            Method setmd = pd.getWriteMethod();
        	
            Object bson = doc.get(field.getName());
            if (null == bson) {
                continue;
            } else if (bson instanceof Document) {// 如果字段是文档了递归调用
                bson = toBean((Document) bson, field.getType());
            } else if (bson instanceof MongoCollection) {// 如果字段是文档集了调用colTOList方法
                bson = colToList(bson, field);
            }
            
            if("class java.lang.String".equals(type)){
            	String value = doc.get(field.getName()).toString();
				setmd.invoke(object, value);
                
            }else if("int".equals(type) || "class java.lang.Integer".equals(type)){
            	Integer value = Integer.valueOf(doc.get(field.getName()).toString());
                setmd.invoke(object, value);
                
            }else if("double".equals(type) || "class java.lang.Double".equals(type)){
            	double value = Double.valueOf(doc.get(field.getName()).toString());
                setmd.invoke(object, value);
                
            }else if("boolean".equals(type) || "class java.lang.Boolean".equals(type)){
            	boolean value = Boolean.valueOf(doc.get(field.getName()).toString());
                setmd.invoke(object, value);
            }
        }
        return object ;
	}
	
	
	/*
     * 将文档集转化为列表
     * 
     * @param:文档集
     * 
     * @param:属性类型
     * 
     * @return:返回列表
     */
    private static List<Object> colToList(Object bson, Field field)
            throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException, IntrospectionException {
        ParameterizedType pt = (ParameterizedType) field.getGenericType();// 获取列表的类型
        List<Object> objs = new ArrayList<Object>();
        @SuppressWarnings("unchecked")
        MongoCollection<Document> cols = (MongoCollection<Document>) bson;
        MongoCursor<Document> cursor = cols.find().iterator();
        while (cursor.hasNext()) {
            Document child = cursor.next();
            @SuppressWarnings("rawtypes")
            Class clz = (Class) pt.getActualTypeArguments()[0];// 获取元素类型
            Object obj = toBean(child, clz);
            System.out.println(child);
            objs.add(obj);

        }
        return objs;
    }
    
    
	
	public static void main(String[] args) {
		
		
		MongoDBTemplet mongoDBTemplet = MongoDBTemplet.getInstance("9.42.89.202", 27017);
			
		 String dbName = "Connection";
		 
		 List<String> list = mongoDBTemplet.getAllCollections(dbName);
		 
		 System.out.println(list);
		 
		 String collectionName = "connection";
		
		 String id = "5b851eab39c4143c11479ead";
		 Db2Connection connection = mongoDBTemplet.findById(dbName, collectionName, id, Db2Connection.class);
		 
		 System.out.println(connection);
		
	}
	
	
}
