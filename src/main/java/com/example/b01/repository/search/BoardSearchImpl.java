
package com.example.b01.repository.search;

import com.example.b01.domain.Board;
import com.example.b01.domain.QBoard;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

// Querydsl을 사용하기 위한 구현 클래스
// BoardSearch 인터페이스를 구현함
public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch{

    // QuerydslRepositorySupport에 어떤 엔티티를 사용할지 지정
    public BoardSearchImpl(){
        super(Board.class);
    }

    @Override
    public Page<Board> search1(Pageable pageable) {

        // QBoard: Querydsl에서 사용하는 엔티티 메타 클래스 (자동 생성됨)
        QBoard board = QBoard.board;

        // from(board): Board 엔티티를 기준으로 쿼리 시작 (select * from board와 유사)
        JPQLQuery<Board> query = from(board);

        // where 조건 추가
        // title 컬럼에 "1"이라는 문자열이 포함된 데이터만 조회
        query.where(board.title.contains("1"));

        // 현재 상태:
        // 조건(where)만 설정된 상태이고,
        // 페이징 처리(Pageable)와 결과 반환 로직은 아직 작성되지 않음

        //SELECT * FROM board WHERE title LIKE "%1%" LIMIT 0,10;
        this.getQuerydsl().applyPagination(pageable,query);

        // fetch()
        // → 작성된 쿼리를 실행해서 실제 데이터 리스트를 조회
        // → 결과는 List<Board> 형태로 반환됨
        // → 페이징이 적용되어 있다면 해당 페이지 데이터만 가져옴
        List<Board> list = query.fetch();

        // fetchCount()
        // → 조건(where)은 그대로 유지한 상태에서 전체 데이터 개수를 조회
        // → 페이징(limit, offset)은 제외하고 count 쿼리 실행
        // → 총 데이터 개수를 알아야 전체 페이지 수 계산 가능
        long count = query.fetchCount();

        return null; // 아직 구현되지 않았기 때문에 null 반환
    }

    @Override
    public Page<Board> searchAll(String[] types, String keyword, Pageable pageable) {

        // QBoard: Querydsl에서 사용하는 엔티티 메타 클래스
        QBoard board = QBoard.board;

        // from(board): Board 테이블 기준으로 조회 시작
        JPQLQuery<Board> query = from(board);

        // 검색 조건(types)과 키워드(keyword)가 모두 존재할 때만 검색 실행
        if( (types != null && types.length > 0) && keyword != null ){

            // BooleanBuilder: 동적 쿼리 (조건을 OR/AND로 조합할 때 사용)
            BooleanBuilder booleanBuilder = new BooleanBuilder(); // (

            // types 배열을 돌면서 어떤 조건으로 검색할지 결정
            // t: title, c: content, w: writer
            for(String type: types){

                switch (type){

                    case "t":
                        // 제목에 keyword 포함
                        booleanBuilder.or(board.title.contains(keyword));
                        break;

                    case "c":
                        // 내용에 keyword 포함
                        booleanBuilder.or(board.content.contains(keyword));
                        break;

                    case "w":
                        // 작성자에 keyword 포함
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                }
            }//end for

            // 위에서 만든 OR 조건들을 where 절에 적용
            query.where(booleanBuilder);

        }//end if


        // 기본 조건 추가 (bno > 0)
        // → 항상 참인 조건으로, 쿼리 안정성이나 기본 필터 역할
        query.where(board.bno.gt(0L));


        // 페이징 적용 (page, size, sort)
        // → LIMIT, OFFSET 자동 적용
        this.getQuerydsl().applyPagination(pageable, query);


        // 실제 데이터 조회 (현재 페이지 데이터)
        List<Board> list = query.fetch();


        // 전체 데이터 개수 조회 (페이징 제외 count 쿼리)
        long count = query.fetchCount();


        // Page 객체로 변환해서 반환
        // list: 현재 페이지 데이터
        // pageable: 페이지 정보
        // count: 전체 데이터 개수
        return new PageImpl<>(list, pageable, count);

//        return  null;
    }
}
