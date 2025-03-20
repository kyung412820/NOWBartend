package com.nowbartend.domain.owner.restaurant.menus.service;

import com.nowbartend.domain.common.auth.service.AuthService;
import com.nowbartend.domain.owner.restaurant.entity.Restaurant;
import com.nowbartend.domain.owner.restaurant.menus.dto.request.MenuCreateRequest;
import com.nowbartend.domain.owner.restaurant.menus.dto.request.MenuUpdateRequest;
import com.nowbartend.domain.owner.restaurant.menus.dto.response.MenuDetailResponse;
import com.nowbartend.domain.owner.restaurant.menus.dto.response.MenuFindResponse;
import com.nowbartend.domain.owner.restaurant.menus.entity.Menu;
import com.nowbartend.domain.owner.restaurant.menus.repository.MenuRepository;
import com.nowbartend.domain.owner.restaurant.service.OwnerRestaurantService;
import com.nowbartend.global.exception.NotFoundException;
import com.nowbartend.global.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final AuthService authService;
    private final OwnerRestaurantService restaurantService;
    private final MenuRepository menuRepository;

    @Transactional
    public void createMenu(Long userId, Long restaurantId, List<MenuCreateRequest> request) {
        Restaurant restaurant = restaurantService.findById(restaurantId);

        authService.checkUserAuthority(restaurant.getOwner().getId(), userId);

        List<Menu> newMenus = request.stream()
                .map(m -> m.toEntity(restaurant))
                .toList();

        menuRepository.saveAll(newMenus);

        // 레스토랑 최대/최소값 변경
        restaurant.updateMinMaxPrice(newMenus);
    }

    @Transactional(readOnly = true)
    public MenuFindResponse findMenus(Long restaurantId) {
        List<Menu> menus = menuRepository.findAllByRestaurantId(restaurantId);

        return MenuFindResponse.from(menus);
    }

    @Transactional(readOnly = true)
    public MenuDetailResponse findMenu(Long restaurantId, Long menuId) {
        Menu menu = findMenuByMenuIdAndRestaurantId(menuId, restaurantId);

        return MenuDetailResponse.from(menu);
    }

    @Transactional
    public void updateMenu(Long userId, Long restaurantId, Long menuId, MenuUpdateRequest request) {
        Menu menu = findMenuByMenuIdAndRestaurantId(menuId, restaurantId);
        Restaurant restaurant = menu.getRestaurant();

        authService.checkUserAuthority(restaurant.getOwner().getId(), userId);

        Integer oldPrice = menu.getPrice();
        Integer newPrice = request.menuPrice();

        menu.update(request.menuName(), newPrice, request.menuDescription());

        // 변경 이전/이후의 가격이 최대/최소값인 경우에만 업데이트 적용
        if (restaurant.isMinOrMaxPrice(oldPrice) || restaurant.isMinOrMaxPrice(newPrice)) {
            restaurant.updateMinMaxPrice();
        }
    }

    @Transactional
    public void deleteMenu(Long userId, Long restaurantId, Long menuId) {
        Menu menu = findMenuByMenuIdAndRestaurantId(menuId, restaurantId);
        Restaurant restaurant = menu.getRestaurant();

        authService.checkUserAuthority(restaurant.getOwner().getId(), userId);

        menuRepository.delete(menu);
        restaurant.removeMenu(menu);

        // 삭제될 메뉴의 가격이 최대 혹은 최소값인 경우에만 업데이트 적용
        if (restaurant.isMinOrMaxPrice(menu.getPrice())) {
            restaurant.updateMinMaxPrice();
        }
    }

    private Menu findMenuByMenuIdAndRestaurantId(Long menuId, Long restaurantId) {
        return menuRepository.findByIdAndRestaurantId(menuId, restaurantId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MENU_NOT_FOUND));
    }
}
