// main.js
// 토큰 검사를 페이지가 언로드될 때 처리
Kakao.init('b5d8b9f0f40b2d55397786378ae4b1a4');

// 토큰 검사 및 리프레시
function checkAuthAndRefresh() {
    const currentTime = Date.now();
    const accessToken = localStorage.getItem('accessToken');
    const refreshToken = localStorage.getItem('refreshToken');
    const accessTokenExpiryTime = parseInt(localStorage.getItem('accessTokenExpiryTime'), 10); // 숫자로 변환

    if (typeof accessToken === "undefined" || !accessToken || accessToken.trim() === '') {
        window.location.href = '/login?error=unauthorized';  // 로그인 화면으로 이동
        return;
    }

    if (isNaN(accessTokenExpiryTime) || currentTime > accessTokenExpiryTime) {
        if (typeof refreshToken !== "undefined" && refreshToken && refreshToken.trim() !== '') {
            api.refreshToken(refreshToken)
                .then(response => {
                    console.log(response); // 서버 응답 확인
                    if (!response || !response.accessToken) {
                        throw new Error("Invalid token response");
                    }

                    const newAccessToken = response.accessToken;
                    const newRefreshToken = response.refreshToken;
                    const newExpiryTime = response.accessTokenExpiryTime;

                    localStorage.setItem('accessToken', newAccessToken);
                    localStorage.setItem('refreshToken', newRefreshToken);
                    localStorage.setItem('accessTokenExpiryTime', newExpiryTime);
                })
                .catch(error => {
                    console.error(error); // 오류 로그 확인
                    window.location.href = '/login?error=unauthorized';  // 로그인 화면으로 이동
                });
        } else {
            window.location.href = '/login?error=unauthorized';  // 로그인 화면으로 이동
        }
    }
}

// api.js
const api = {
    refreshToken: (refreshToken) => {
        return fetch('/api/auth/refresh', {
            method: 'POST',
            headers: {
                "Content-Type": 'application/json'
            },
            body: JSON.stringify(
                {
                    refreshToken: refreshToken
                }
            )
        })
            .then(response => response.json())
    },

    fetchRestaurants: async (accessToken, params) => {
        const url = `/customer/api/restaurants?${params.toString()}`;
        try {
            const response = await fetch(url, {
                method: "GET",
                headers: {
                    "Authorization": `Bearer ${accessToken}`,
                    "Content-Type": "application/json"
                }
            });

            if (!response.ok) {
                throw new Error("가게 목록을 가져오는 데 실패했습니다.");
            }

            return await response.json();
        } catch (error) {
            console.error(error);
            throw error;
        }
    }
};

// event.js

let currentPage = 0; // 현재 페이지 번호
let isLoading = false; // 요청 중인지 여부
let hasMoreData = true; // 더 가져올 데이터가 있는지 여부

function goToStore(storeId) {
    window.location.href = '/restaurant?id=' + storeId;
}

// 검색 시 거리값 함께 전송
function searchStores(reset = true) {
    checkAuthAndRefresh();

    if (reset) {
        currentPage = 0;
        hasMoreData = true;
        document.getElementById("store-list").innerHTML = ""; // 기존 리스트 초기화
    }

    if (isLoading || !hasMoreData) return;
    isLoading = true;

    const keyword = document.getElementById("search-input").value;
    const address = document.getElementById("address").value;
    const minPrice = parseInt(minPriceSlider.value);
    const maxPrice = parseInt(maxPriceSlider.value);
    const seatType = document.getElementById("seat-type").value;
    const isUseDistance = document.getElementById("nearby-btn").classList.contains("active");
    const order = document.querySelector(".sort-option.selected")?.dataset.sort || "popularity";
    const distance = getSelectedDistance();

    console.log(keyword);
    console.log(address);
    console.log(minPrice);
    console.log(maxPrice);
    console.log(seatType);
    console.log(isUseDistance);
    console.log(order);
    console.log(distance);

    let clientLat = null;
    let clientLon = null;

    if (isUseDistance && "geolocation" in navigator) {
        navigator.geolocation.getCurrentPosition((position) => {
            clientLat = position.coords.latitude;
            clientLon = position.coords.longitude;
            fetchRestaurants(keyword, address, minPrice, maxPrice, seatType, isUseDistance, clientLat, clientLon, order, distance);
        }, (error) => {
            console.error("위치 정보를 가져오는 데 실패했습니다.", error);
            fetchRestaurants(keyword, address, minPrice, maxPrice, seatType, isUseDistance, clientLat, clientLon, order, distance);
        });
    } else {
        fetchRestaurants(keyword, address, minPrice, maxPrice, seatType, isUseDistance, clientLat, clientLon, order, distance);
    }
}

async function fetchRestaurants(keyword, address, minPrice, maxPrice, seatType, isUseDistance, clientLat, clientLon, order, distance) {
    const params = new URLSearchParams();

    if (keyword !== undefined && keyword !== null) params.append("keyword", keyword);
    if (address !== undefined && address !== null) params.append("address", address);
    if (minPrice !== undefined && minPrice !== null) params.append("minPrice", minPrice);
    if (maxPrice !== undefined && maxPrice !== null) params.append("maxPrice", maxPrice);
    if (seatType !== undefined && seatType !== null) params.append("seatTypeId", seatType);
    if (order !== undefined && order !== null) params.append("order", order);
    if (isUseDistance !== undefined && isUseDistance !== null) {
        params.append("isUseDistance", isUseDistance ? "true" : "false");

        // isUseDistance가 true일 때, 필요한 값들이 모두 존재하는지 확인
        if (isUseDistance) {
            console.log("내 주변 검색")
            if (clientLat === undefined || clientLat === null) {
                throw new Error("clientLat is required when isUseDistance is true");
            }
            if (clientLon === undefined || clientLon === null) {
                throw new Error("clientLon is required when isUseDistance is true");
            }
            if (distance === undefined || distance === null) {
                throw new Error("distance is required when isUseDistance is true");
            }

            // 값이 모두 존재하면 params에 추가
            params.append("clientLat", clientLat);
            params.append("clientLon", clientLon);
            params.append("distance", distance);
        }
    }

    params.append("page", currentPage);

    const accessToken = localStorage.getItem('accessToken');

    try {
        const data = await api.fetchRestaurants(accessToken, params);
        console.log("가게 조회 완료")

        if (data.content.length === 0) {
            hasMoreData = false; // 더 이상 가져올 데이터 없음
            console.log("가게 목록 없음")
        } else {
            displayRestaurants(data.content);
            currentPage++; // 페이지 번호 증가
        }
    } catch (error) {
        console.error(error);
    } finally {
        isLoading = false;
    }
}

function displayRestaurants(restaurants) {
    const storeList = document.getElementById("store-list");
    console.log("가게 목록")

    restaurants.forEach(restaurant => {
        const storeItem = document.createElement("div");
        storeItem.classList.add('store-item');
        storeItem.innerHTML = `
                <div class="store-header">
                    <div class="store-name">
                        <h2>${restaurant.name}</h2>
                        <button class="bookmark-button ${restaurant.isBookmark ? 'active' : ''}"
                                onclick="toggleBookmark('${restaurant.id}', this)">
                            <i class="fas fa-bookmark"></i>
                        </button>
                    </div>
                </div>
                <div class="store-address">
                    <p>${restaurant.address}</p>
                </div>
                <div class="store-info">
                    <p>${restaurant.openTime} - ${restaurant.closeTime} | 가격: ${restaurant.minPrice} ~ ${restaurant.maxPrice}원</p>
                </div>
            `;

        storeList.appendChild(storeItem);

    });
}

// 무한 스크롤 이벤트 추가
let debounceTimeout;
window.addEventListener("scroll", () => {
    clearTimeout(debounceTimeout);
    debounceTimeout = setTimeout(() => {
        if (window.innerHeight + window.scrollY >= document.body.offsetHeight - 500) {
            searchStores(false); // 추가 데이터 로드
        }
    }, 200);  // 200ms마다 호출
});

// 필터 검색
function openSortModal() {
    document.getElementById("sort-modal").style.display = "flex";
}

function closeSortModal() {
    document.getElementById("sort-modal").style.display = "none";
}

function selectSort(event) {
    const selectedOption = event.currentTarget;
    if (!selectedOption) return;

    const sortValue = selectedOption.dataset.sort;
    console.log("선택된 정렬 기준:", sortValue);

    document.querySelectorAll('.sort-option').forEach(option => {
        option.classList.remove('selected');
    });

    selectedOption.classList.add('selected');
}

document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll(".sort-option").forEach(option => {
        option.addEventListener("click", selectSort);
    });
});

function openFilterModal() {
    document.getElementById("filter-modal").style.display = "flex";
}

function closeFilterModal() {
    document.getElementById("filter-modal").style.display = "none";
}

function applyFilters() {
    closeFilterModal();
}

// 가격 조정
const minPriceSlider = document.getElementById('min-price');
const maxPriceSlider = document.getElementById('max-price');
const minPriceDisplay = document.getElementById('min-price-display');
const maxPriceDisplay = document.getElementById('max-price-display');

function updatePriceDisplay() {
    let minPrice = parseInt(minPriceSlider.value);
    let maxPrice = parseInt(maxPriceSlider.value);

    // 두 슬라이더의 값이 겹치지 않도록 조정
    if (minPrice >= maxPrice) {
        minPriceSlider.value = maxPrice - 10000; // 최소값은 최대값보다 10000 작게 설정
    }

    if (maxPrice <= minPrice) {
        maxPriceSlider.value = minPrice + 10000; // 최대값은 최소값보다 10000 크게 설정
    }

    // 값 표시 업데이트
    minPriceDisplay.textContent = minPrice;
    maxPriceDisplay.textContent = maxPrice;
}

const nearbyBtn = document.getElementById('nearby-btn');
const distanceSlider = document.getElementById('distance');
const distanceDisplay = document.getElementById('distance-display');
const values = [100, 200, 500, 1000, 2000, 3000];

// 슬라이더의 값이 변경될 때마다 텍스트 업데이트
distanceSlider.addEventListener("input", function () {
    const selectedValue = values[distanceSlider.value];  // 슬라이더 값에 해당하는 값 선택

    if (selectedValue <= 1000) {
        distanceDisplay.textContent = `${selectedValue}m`;
    } else {
        distanceDisplay.textContent = `${selectedValue / 1000}km`;
    }
});

// '내 주변' 버튼 클릭 시 거리 선택 영역 표시
nearbyBtn.addEventListener('click', () => {
    // 버튼에 active 클래스를 토글 (선택된 상태로 만들기)
    nearbyBtn.classList.toggle('active');

    // '내 주변' 선택 시 distance 영역 표시
    const distanceContainer = document.getElementById('distance-container');
    distanceContainer.style.display = nearbyBtn.classList.contains('active') ? 'block' : 'none';
});

function getSelectedDistance() {
    return values[distanceSlider.value];
}
