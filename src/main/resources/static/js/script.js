Kakao.init('ba33a667b3286f103e25aad5ed702281');y

async function toggleBookmark(restaurantId, buttonElement) {
    const accessToken = localStorage.getItem('accessToken');
    if (!accessToken) {
        alert('로그인이 필요합니다.');
        return;
    }

    const isBookmarked = buttonElement.classList.contains('active');
    const bookmarkCreateReq = {restaurantId: restaurantId};

    try {
        let response;
        if (isBookmarked) {
            removeBookmark(restaurantId);
            console.log("북마크 제거");
        } else {
            response = await fetch(BOOKMARK_API_URL, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${accessToken}`
                },
                body: JSON.stringify(bookmarkCreateReq)
            });
            console.log("북마크 추가");
        }

        if (response && response.ok) {
            buttonElement.classList.toggle('active');
            displayBookmarkedStores();
        } else {
            console.error('북마크 실패:', response ? await response.json() : 'Network error');
            alert('북마크 실패!');
        }
    } catch (error) {
        console.error('북마크 오류:', error);
        alert('북마크 오류!');
    }
}

async function removeBookmark(restaurantId) {
    const accessToken = localStorage.getItem('accessToken');

    try {
        const response = await fetch(`${BOOKMARK_API_URL}/${restaurantId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${accessToken}`
            }
        });

        if (response.ok) {
            alert('북마크가 취소되었습니다.');
            displayBookmarkedStores();
        } else {
            console.error('북마크 취소 실패:', response);
            alert('북마크 취소 실패!');
        }
    } catch (error) {
        console.error('북마크 취소 중 오류:', error);
        alert('북마크 취소 중 오류가 발생했습니다.');
    }
}

async function displayBookmarkedStores() {
    const bookmarkedStoresElement = document.getElementById('bookmarked-stores');
    bookmarkedStoresElement.innerHTML = '';
    const accessToken = localStorage.getItem('accessToken');

    if (!accessToken) {
        alert('로그인이 필요합니다.');
        return;
    }

    try {
        const response = await fetch(BOOKMARK_API_URL, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${accessToken}`
            }
        });

        if (response.ok) {
            const bookmarksFindResponse = await response.json();
            const bookmarkedStores = bookmarksFindResponse.bookmarks;

            bookmarkedStores.forEach(store => {
                const storeItem = document.createElement('div');
                storeItem.classList.add('bookmarked-store-item');
                storeItem.innerHTML = `
                    <h2>${store.restaurantName}</h2>
                    <button onclick="removeBookmark('${store.bookmarkId}')">
                        <i class="fas fa-bookmark"></i> 북마크 취소
                    </button>
                `;
                bookmarkedStoresElement.appendChild(storeItem);
            });
        } else {
            console.error('북마크 목록 가져오기 실패:', response);
            alert('북마크 목록을 가져오는데 실패했습니다.');
        }
    } catch (error) {
        console.error('북마크 목록 가져오기 중 오류:', error);
        alert('북마크 목록을 가져오는 중에 오류가 발생했습니다.');
    }
}

async function displayPosts(posts) {
    const postListElement = document.getElementById('post-list');
    postListElement.innerHTML = '';

    posts.forEach(post => {
        const postItem = document.createElement('div');
        postItem.classList.add('post-item');
        postItem.innerHTML = `
            <h2>${post.title}</h2>
            <p>작성자: ${post.author}</p>
            <p>${post.content}</p>
            <button class="follow-button ${post.isFollowing ? 'following' : ''}"
                    onclick="toggleFollow('${post.author}', this)">
                ${post.isFollowing ? '팔로우 중' : '팔로우'}
            </button>
            <button class="like-button ${post.liked ? 'active' : ''}"
                    onclick="toggleLike('${post.id}', this)">
                <i class="fas fa-heart"></i> ${post.likeCount}
            </button>
            <button onclick="goToComment('${post.id}')">
                댓글 (${post.commentCount})
            </button>
        `;
        postListElement.appendChild(postItem);
    });
}

function goToComment(postId) {
    window.location.href = 'comment.html?postId=' + postId;
}

async function createComment() {
    const postId = new URLSearchParams(window.location.search).get('postId');
    const commentContent = document.getElementById('comment-input').value;
    // TODO: 백엔드 API 호출하여 댓글 생성
    alert('댓글 작성: ' + commentContent + ' (게시글 ID: ' + postId + ')');
    displayComments(postId);
}

async function toggleCommentLike(commentId, buttonElement) {
    // TODO: 백엔드 API 호출하여 댓글 좋아요/취소
    buttonElement.classList.toggle('active');
}

async function displayComments(postId) {
    const commentListElement = document.getElementById('comment-list');
    commentListElement.innerHTML = '';

    // TODO: 백엔드 API 호출하여 댓글 목록 가져오기
    const comments = [
        {id: 'comment1', author: '사용자 3', content: '댓글 내용 1', liked: false, likeCount: 3},
        {id: 'comment2', author: '사용자 4', content: '댓글 내용 2', liked: true, likeCount: 7}
    ];

    comments.forEach(comment => {
        const commentItem = document.createElement('div');
        commentItem.classList.add('comment-item');
        commentItem.innerHTML = `
            <p>작성자: ${comment.author}</p>
            <p>${comment.content}</p>
            <button class="like-button ${comment.liked ? 'active' : ''}"
                    onclick="toggleCommentLike('${comment.id}', this)">
                <i class="fas fa-heart"></i> ${comment.likeCount}
            </button>
        `;
        commentListElement.appendChild(commentItem);
    });
}

async function displayPostDetails(postId) {
    const postDetailsElement = document.getElementById('post-details');
    postDetailsElement.innerHTML = '';

    // TODO: 백엔드 API 호출하여 게시글 정보 가져오기
    const post = {
        id: postId,
        author: '사용자 1',
        title: '글 제목 1',
        content: '게시글 내용 1'
    };

    postDetailsElement.innerHTML = `
        <h2>${post.title}</h2>
        <p>작성자: ${post.author}</p>
        <p>${post.content}</p>
    `;
}

async function displayReservations() {
    const reservationListElement = document.getElementById('reservation-list');
    reservationListElement.innerHTML = '';

    // TODO: 백엔드 API 호출하여 예약 목록 가져오기
    const reservations = [
        {id: 'reservation1', storeName: '가게 1', time: '2024-01-01 12:00'},
        {id: 'reservation2', storeName: '가게 2', time: '2024-01-02 18:00'}
    ];

    reservations.forEach(reservation => {
        const reservationItem = document.createElement('div');
        reservationItem.classList.add('reservation-item');
        reservationItem.innerHTML = `
            <h2>${reservation.storeName}</h2>
            <p>${reservation.time}</p>
            <button onclick="goToCancelReservation('${reservation.id}')">취소</button>
        `;
        reservationListElement.appendChild(reservationItem);
    });
}

function goToCancelReservation(reservationId) {
    window.location.href = 'cancel_reservation.html?id=' + reservationId;
}

function cancelReservation() {
    const reservationId = new URLSearchParams(window.location.search).get('id');
    // TODO: 백엔드 API 호출하여 예약 취소
    alert('예약 취소: ' + reservationId);
}

async function searchPosts() {
    const searchTerm = document.getElementById('post-search-input').value;
    // TODO: 백엔드 API 호출하여 검색 결과 가져오기
    const searchResults = [
        {
            id: 'post1',
            author: '사용자 1',
            title: '글 제목 1',
            content: '게시글 내용 1',
            isFollowing: true,
            likeCount: 10,
            commentCount: 5,
            liked: false
        },
        {
            id: 'post2',
            author: '사용자 2',
            title: '글 제목 2',
            content: '게시글 내용 2',
            isFollowing: false,
            likeCount: 15,
            commentCount: 8,
            liked: true
        }
    ];
    displayPosts(searchResults);
}

async function toggleFollow(userId, buttonElement) {
    // TODO: 백엔드 API 호출하여 팔로우/언팔로우
    const isFollowing = buttonElement.classList.contains('following');
    if (isFollowing) {
        buttonElement.classList.remove('following');
        buttonElement.textContent = '팔로우';
    } else {
        buttonElement.classList.add('following');
        buttonElement.textContent = '팔로우 중';
    }
}

async function toggleLike(postId, buttonElement) {
    // TODO: 백엔드 API 호출하여 좋아요/취소
    buttonElement.classList.toggle('active');
}

// 쿠폰 페이지
async function fetchCoupons() {
    const storeId = new URLSearchParams(window.location.search).get('id');
    const response = await fetch(`/customer/api/coupons?restaurantId=${storeId}`);
    const coupons = await response.json();
    const couponList = document.querySelector('.coupon-list');
    coupons.forEach(coupon => {
        const couponItem = document.createElement('div');
        couponItem.innerHTML = `
            <h2>${coupon.couponName}</h2>
            <p>${coupon.discount}% 할인</p>
            <button onclick="downloadCoupon(${coupon.couponId}, ${storeId})">다운로드</button>
        `;
        couponList.appendChild(couponItem);
    });
}

async function downloadCoupon(couponId, restaurantId) {
    const accessToken = localStorage.getItem('accessToken');
    if (!accessToken) {
        alert('로그인이 필요합니다.');
        return;
    }

    try {
        const response = await fetch(`/customer/api/coupons/${couponId}/download/?restaurantId=${restaurantId}`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${accessToken}`
            }
        });

        if (response.ok) {
            alert('쿠폰 다운로드 성공!');
        } else {
            const errorData = await response.json();
            alert('쿠폰 다운로드 실패: ' + errorData.message);
        }
    } catch (error) {
        alert('오류 발생: ' + error);
    }
}

// 메뉴 페이지
async function fetchMenu() {
    const storeId = new URLSearchParams(window.location.search).get('id');
    const response = await fetch(`/api/restaurants/${storeId}/menus`);
    const menus = await response.json();
    const menuList = document.querySelector('.menu-list');
    menus.forEach(menu => {
        const menuItem = document.createElement('div');
        menuItem.innerHTML = `
            <h2>${menu.menuName}</h2>
            <p>${menu.menuPrice}원</p>
            <p>${menu.menuDescription}</p>
        `;
        menuList.appendChild(menuItem);
    });
}

// 예약 페이지
async function fetchStoreInfo() {
    const storeId = new URLSearchParams(window.location.search).get('id');
    const response = await fetch(`/api/restaurants/${storeId}`);
    const store = await response.json();
    document.getElementById('store-name').textContent = `가게 이름: ${store.name}`;
    document.getElementById('store-address').textContent = `주소: ${store.address}`;

    const seatTypeSelect = document.getElementById('seat-type');
    store.seatTypes.forEach(seatType => {
        const option = document.createElement('option');
        option.value = seatType.id;
        option.textContent = `${seatType.name} (${seatType.minGuestCount} ~ ${seatType.maxGuestCount}명)`;
        seatTypeSelect.appendChild(option);
    });
}

async function makeReservation() {
    const storeId = new URLSearchParams(window.location.search).get('id');
    const date = document.getElementById('reservation-date').value;
    const people = document.getElementById('reservation-people').value;
    const seatTypeId = document.getElementById('seat-type').value;

    const accessToken = localStorage.getItem('accessToken');
    if (!accessToken) {
        alert('로그인이 필요합니다.');
        return;
    }

    try {
        const response = await fetch(`/customer/api/reservations`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${accessToken}`
            },
            body: JSON.stringify({
                restaurantId: parseInt(storeId),
                date: date,
                totalCount: parseInt(people),
                seatTypeId: parseInt(seatTypeId)
            })
        });

        if (response.ok) {
            alert('예약 성공!');
        } else {
            const errorData = await response.json();
            alert('예약 실패: ' + errorData.message);
        }
    } catch (error) {
        alert('오류 발생: ' + error);
    }
}

// 웨이팅 페이지
async function fetchWaitingInfo() {
    const storeId = new URLSearchParams(window.location.search).get('id');
    const response = await fetch(`/customer/api/restaurants/${storeId}/waitings`);
    const waitingInfo = await response.json();
    document.getElementById('current-waiting-number').textContent = waitingInfo.rank || '대기열에 없습니다.';
    document.getElementById('total-waiting-people').textContent = waitingInfo.inRestaurantWaitingSize + waitingInfo.takeOutWaitingSize;
}

async function joinWaiting() {
    const storeId = new URLSearchParams(window.location.search).get('id');
    const people = document.getElementById('waiting-people').value;
    const waitingType = document.getElementById('waiting-type').value;

    const accessToken = localStorage.getItem('accessToken');
    if (!accessToken) {
        alert('로그인이 필요합니다.');
        return;
    }

    try {
        const response = await fetch(`/customer/api/restaurants/${storeId}/waitings`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${accessToken}`
            },
            body: JSON.stringify({
                totalCount: parseInt(people),
                waitingType: waitingType
            })
        });

        if (response.ok) {
            alert('웨이팅 등록 성공!');
            fetchWaitingInfo();
        } else {
            const errorData = await response.json();
            alert('웨이팅 등록 실패: ' + errorData.message);
        }
    } catch (error) {
        alert('오류 발생: ' + error);
    }
}

async function cancelWaiting() {
    const storeId = new URLSearchParams(window.location.search).get('id');
    const waitingId = new URLSearchParams(window.location.search).get('waitingId');

    const accessToken = localStorage.getItem('accessToken');
    if (!accessToken) {
        alert('로그인이 필요합니다.');
        return;
    }

    try {
        const response = await fetch(`/customer/api/restaurants/${storeId}/waitings/${waitingId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${accessToken}`
            }
        });

        if (response.ok) {
            alert('웨이팅 취소 성공!');
            fetchWaitingInfo();
        } else {
            const errorData = await response.json();
            alert('웨이팅 취소 실패: ' + errorData.message);
        }
    } catch (error) {
        alert('오류 발생: ' + error);
    }
}

async function refreshWaiting() {
    fetchWaitingInfo();
}

window.onload = () => {
    if (window.location.pathname.includes('collection.html')) {
        displayBookmarkedStores();
    } else if (window.location.pathname.includes('post.html')) {
        searchPosts();
    } else if (window.location.pathname.includes('history.html')) {
        displayReservations();
    } else if (window.location.pathname.includes('coupon.html')) {
        fetchCoupons();
    } else if (window.location.pathname.includes('menu.html')) {
        fetchMenu();
    } else if (window.location.pathname.includes('reservation.html')) {
        fetchStoreInfo();
    } else if (window.location.pathname.includes('waiting.html')) {
        fetchWaitingInfo();
    } else if (window.location.pathname.includes('comment.html')) {
        const postId = new URLSearchParams(window.location.search).get('postId');
        displayPostDetails(postId);
        displayComments(postId);
    } else if (window.location.pathname.includes('index.html')) {
        searchStores();
    }
};
