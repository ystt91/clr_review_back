package org.zerock.mallapi.repository;


import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.mallapi.domain.Member;

public interface MemberRepository extends JpaRepository<Member, String> {

    //배열처럼 {"memberRoleList"}
    @EntityGraph(attributePaths = {"memberRoleList"})
    @Query("select m from Member m where m.email = :email")
    Member getWithRoles(@Param("email") String email);


}
