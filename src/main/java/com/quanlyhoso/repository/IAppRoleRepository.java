package com.quanlyhoso.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.quanlyhoso.entity.AppRole;
import com.quanlyhoso.entity.CategoryPK;

@Repository
public interface IAppRoleRepository extends JpaRepository<AppRole, CategoryPK> {

	@Query("SELECT e FROM AppRole e " + "Where e.roleName = :roleName")
	public AppRole getOneByRoleName(@Param("roleName") String roleName);
}
