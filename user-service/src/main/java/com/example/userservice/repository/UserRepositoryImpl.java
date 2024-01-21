package com.example.userservice.repository;

import com.example.userservice.domain.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(UserEntity userEntity) {
        entityManager.persist(userEntity);
    }

    @Override
    public Optional<UserEntity> findByUserId(String userId) {
        return entityManager.createQuery("select u from UserEntity u where u.userId =: userId" , UserEntity.class)
                .setParameter("userId" , userId)
                .getResultList().stream().findFirst();
    }

    @Override
    public List<UserEntity> findAll(){
        return entityManager.createQuery("select u from UserEntity u", UserEntity.class).getResultList();
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return entityManager.createQuery("select u from UserEntity u where u.email =: email" , UserEntity.class)
                .setParameter("email" , email)
                .getResultList().stream().findFirst();
    }
    /**
     * jpa에서 stream을 사용할 때 try-with-resources로 감싸주지 않으면 stream이 닫히지 않은 경우 db커넥션도 닫히지 않기 때문에 예외 처리를 해주어야 한다.
     * 이는 stream을 사용하면 디폴트로 lazy loading방식으로 데이터를 로드하기 때문이다.
     * stream의 데이터를 사용 할 때 조회해야 하기 때문에 stream이 닫히기 전까진 db커넥션을 닫을 수 없는 것이다.
     * */
}
