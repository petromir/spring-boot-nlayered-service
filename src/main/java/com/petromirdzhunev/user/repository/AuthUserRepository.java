package com.petromirdzhunev.user.repository;

import static com.petromirdzhunev.user.repository.jooq.Tables.AUTH_USER_DB_TABLE;
import static com.petromirdzhunev.user.repository.jooq.Tables.AUTH_USER_ROLES_DB_TABLE;
import static com.petromirdzhunev.user.repository.jooq.Tables.AUTH_USER_ROLE_DB_TABLE;
import static org.jooq.Records.mapping;
import static org.jooq.impl.DSL.multiset;
import static org.jooq.impl.DSL.select;

import java.util.List;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.InsertValuesStep2;
import org.jooq.Record;
import org.springframework.stereotype.Component;

import com.petromirdzhunev.domain.core.exception.EntityAlreadyExistsException;
import com.petromirdzhunev.domain.core.exception.EntityNotFoundException;
import com.petromirdzhunev.user.entity.AuthUser;
import com.petromirdzhunev.user.entity.AuthUserRole;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthUserRepository {

	private final DSLContext db;

	public AuthUser insertAuthUser(final AuthUser authUser) {
		final List<String> roleCodes = authUser.getRoles()
		                                       .stream()
		                                       .map(AuthUserRole::code)
		                                       .toList();

		final List<Long> roleIds = db.select(AUTH_USER_ROLE_DB_TABLE.ID)
		                             .from(AUTH_USER_ROLE_DB_TABLE)
		                             .where(AUTH_USER_ROLE_DB_TABLE.CODE.in(roleCodes))
		                             .fetch(AUTH_USER_ROLE_DB_TABLE.ID);

		if (roleIds.isEmpty()) {
			throw new EntityNotFoundException(
					"No auth user roles found [codes=%s]".formatted(StringUtils.join(roleCodes, ",")));
		}

		final Long authUserId = db.insertInto(AUTH_USER_DB_TABLE)
		                          .set(AUTH_USER_DB_TABLE.EMAIL, authUser.getEmail())
		                          .set(AUTH_USER_DB_TABLE.PASSWORD, authUser.getPassword())
		                          .set(AUTH_USER_DB_TABLE.FIRST_NAME, authUser.getFirstName())
		                          .set(AUTH_USER_DB_TABLE.LAST_NAME, authUser.getLastName())
		                          .returning(AUTH_USER_DB_TABLE.ID)
		                          .fetchOptional(AUTH_USER_DB_TABLE.ID)
		                          .orElseThrow(() -> new EntityNotFoundException(
				                          "Auth user not inserted [email=%s]".formatted(authUser.getEmail())));

		InsertValuesStep2<Record, Long, Long> insertStep = db.insertInto(
				AUTH_USER_ROLES_DB_TABLE, AUTH_USER_ROLES_DB_TABLE.USER_ID, AUTH_USER_ROLES_DB_TABLE.ROLE_ID);
		for (final Long roleId : roleIds) {
			insertStep = insertStep.values(authUserId, roleId);
		}
		insertStep.execute();

		return authUser.withoutPassword();
	}

	public AuthUser authUser(final String authUserEmail) {
		return this.authUser(AUTH_USER_DB_TABLE.EMAIL.eq(authUserEmail),
				() -> new EntityNotFoundException("No existing auth user found for [email=%s]"
						.formatted(authUserEmail)));
	}

	public AuthUser authUser(final Long authUserId) {
		return this.authUser(AUTH_USER_DB_TABLE.ID.eq(authUserId),
				() -> new EntityNotFoundException("No existing auth user found for [id=%d]".formatted(authUserId)));
	}

	public void deleteAuthUser(final Long authUserId) {
		db.deleteFrom(AUTH_USER_DB_TABLE)
		  .where(AUTH_USER_DB_TABLE.ID.eq(authUserId))
		  .execute();
	}

	public void assertAuthUserNotExists(final String authUserEmail) {
		boolean userExists =
				db.fetchExists(
						db.selectOne()
						  .from(AUTH_USER_DB_TABLE)
						  .where(AUTH_USER_DB_TABLE.EMAIL.eq(authUserEmail)));

		if (userExists) {
			throw new EntityAlreadyExistsException("Username already exists for [name=%s]");
		}
	}

	private AuthUser authUser(final Condition condition, final Supplier<EntityNotFoundException> exceptionSupplier) {
		return db.select(AUTH_USER_DB_TABLE.ID,
			         AUTH_USER_DB_TABLE.EMAIL,
			         AUTH_USER_DB_TABLE.PASSWORD,
			         AUTH_USER_DB_TABLE.FIRST_NAME,
			         AUTH_USER_DB_TABLE.LAST_NAME,
			         multiset(
				         select(
					         AUTH_USER_ROLE_DB_TABLE.ID, AUTH_USER_ROLE_DB_TABLE.CODE, AUTH_USER_ROLE_DB_TABLE.NAME
				         ).from(AUTH_USER_ROLES_DB_TABLE)
				          .join(AUTH_USER_ROLE_DB_TABLE).on(AUTH_USER_ROLE_DB_TABLE.ID.eq(AUTH_USER_ROLES_DB_TABLE.ROLE_ID))
						         .and(AUTH_USER_ROLES_DB_TABLE.USER_ID.eq(AUTH_USER_DB_TABLE.ID))
			         ).as("roles")
			          .convertFrom(r -> r.map(record ->
					          new AuthUserRole(
						          record.getValue(AUTH_USER_ROLE_DB_TABLE.ID),
						          record.getValue(AUTH_USER_ROLE_DB_TABLE.CODE),
						          record.getValue(AUTH_USER_ROLE_DB_TABLE.NAME))
				          )
			          ))
		         .from(AUTH_USER_DB_TABLE)
		         .where(condition)
		         .fetchOptional()
		         .map(mapping((id, email, password, firstName, lastName, authUserRoles) ->
				         AuthUser.builder()
				                 .id(id)
				                 .email(email)
				                 .password(password)
				                 .firstName(firstName)
				                 .lastName(lastName)
				                 .roles(authUserRoles)
				                 .build()))
		         .orElseThrow(exceptionSupplier);
	}
}
