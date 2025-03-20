package com.nowbartend.domain.customer.post.service;

import com.nowbartend.domain.customer.post.dto.response.PostCounts;
import com.nowbartend.domain.customer.post.dto.response.PostKeywordResponse;
import com.nowbartend.domain.customer.post.entity.PostDocument;
import com.nowbartend.domain.customer.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostElasticService {

    private final ElasticsearchOperations elasticsearchOperations;
    private final PostRepository postRepository;

    public Slice<PostKeywordResponse> findPostsByKeywordWithElastic(
            Long regionId,
            Long cursorValue,
            String keyword,
            int limit
    ) {
        // Bool 쿼리 구성
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        if (regionId != null) {
            boolQuery.must(QueryBuilders.termQuery("region_id", regionId));
        }

        if (cursorValue != null) {
            boolQuery.must(QueryBuilders.rangeQuery("post_id").lt(cursorValue));
        }

        // 키워드 검색: title, content, userName
        BoolQueryBuilder keywordQuery = QueryBuilders.boolQuery()
                .should(QueryBuilders.matchQuery("title", keyword).operator(org.elasticsearch.index.query.Operator.AND))
                .should(QueryBuilders.matchQuery("content", keyword).operator(org.elasticsearch.index.query.Operator.AND))
                .should(QueryBuilders.matchQuery("nickname", keyword).operator(org.elasticsearch.index.query.Operator.AND))
                .minimumShouldMatch(1);
        boolQuery.must(keywordQuery);

        PageRequest pageRequest = PageRequest.of(0, limit + 1, Sort.by(Sort.Direction.DESC, "post_id"));

        String base64EncodedQuery = Base64.getEncoder().encodeToString(
                boolQuery.toString().getBytes(StandardCharsets.UTF_8)
        );

        // NativeQueryBuilder를 사용하여 쿼리 생성 (최신 API)
        NativeQuery query = new NativeQueryBuilder()
                .withQuery(q -> q.wrapper(w -> w.query(base64EncodedQuery)))
                .withPageable(pageRequest)
                .build();

        // ElasticsearchOperations를 사용하여 검색 수행
        SearchHits<PostDocument> searchHits = elasticsearchOperations.search(query, PostDocument.class);
        List<PostDocument> postDocuments = searchHits.stream()
                .map(SearchHit::getContent)
                .toList();

        List<Long> postIds = searchHits.stream().map(hit -> Long.valueOf(hit.getContent().getId())).toList();

        List<PostCounts> postCounts = postRepository.findAllByIdWithCommentsAndLikes(postIds);

        Map<Long, PostCounts> postCountMap = postCounts.stream()
                .collect(Collectors.toMap(PostCounts::postId, Function.identity()));

        // 결과 목록 추출
        List<PostKeywordResponse> responses = postDocuments.stream()
                .map(doc -> {
                    Long postId = Long.valueOf(doc.getId());

                    PostCounts postCount = postCountMap.get(postId);
                    Integer commentCount = (postCount.commentCount() != null) ? postCount.commentCount() : 0;
                    Integer likeCount = (postCount.likeCount() != null) ? postCount.likeCount() : 0;
                    return PostKeywordResponse.from(doc, commentCount, likeCount);
                })
                .toList();

        // 다음 페이지 여부 판단
        boolean hasNext = responses.size() == limit + 1;
        if (hasNext) {
            responses = responses.subList(0, limit);
        }

        return new SliceImpl<>(responses, PageRequest.of(0, limit), hasNext);
    }
}
