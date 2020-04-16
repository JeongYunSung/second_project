package com.yunseong.second_project.cart.infra.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yunseong.second_project.cart.domain.CartItem;
import com.yunseong.second_project.cart.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CartRepositoryImpl implements CartRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public void addItem(String id, CartItem item) {
        final String key = String.format("cart:%s", id);

        this.redisTemplate.execute(new SessionCallback<List<Object>>() {
            @Override
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                operations.watch(key);

                try {
                    operations.multi();
                    operations.opsForList().rightPush(key, item);

                } catch (Exception e) {
                    operations.discard();
                }
                return operations.exec();
            }
        });
    }

    public List<CartItem> findCartItemList(String id) {
        final String key = String.format("cart:%s", id);

        List<CartItem> range = this.redisTemplate.opsForList()
                .range(key, 0, -1)
                .stream().map(item -> this.objectMapper.convertValue(item, CartItem.class))
                .collect(Collectors.toList());

        return range;
    }

    public boolean removeCartItem(String id, Long idx) {
        final String key = String.format("cart:%s", id);

        List<Object> range = this.redisTemplate.opsForList().range(key, 0, -1);
        for (int i = 0; i < range.size(); i++) {
            CartItem cartItem = this.objectMapper.convertValue(range.get(i), CartItem.class);
            if(cartItem.getProductId() == idx) {
                Long value = this.redisTemplate.opsForList().remove(key, 0, cartItem);
                return value != null && value == 1;
            }
        }

        return false;
    }

    public void deleteCart(String id) {
        final String key = String.format("cart:%s", id);

        Boolean delete = this.redisTemplate.delete(key);
    }
}
