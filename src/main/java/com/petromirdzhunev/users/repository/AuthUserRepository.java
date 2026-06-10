package com.petromirdzhunev.users.repository;

import static com.inteldecisions.infra.db.jooq.Tables.AUTH_USER_DB_TABLE;
import static com.inteldecisions.infra.db.jooq.Tables.AUTH_USER_ROLE_DB_TABLE;
import static org.jooq.Records.mapping;
import static org.jooq.impl.DSL.multiset;
import static org.jooq.impl.DSL.select;

import java.util.function.Supplier;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;

import com.petromirdzhunev.users.entity.AuthUser;
import com.petromirdzhunev.users.entity.AuthUserRole;
import com.petromirdzhunev.exception.EntityAlreadyExistsException;
import com.petromirdzhunev.exception.EntityNotFoundException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthUserRepository {

	private final DSLContext db;

	public AuthUser insertAuthUser(final AuthUser authUser) {
		// NOTE: Support multiple roles
		final String authUserRoleCode = authUser.getRoles().getFirst().code();
		final long authUserRoleId = db.select(AUTH_USER_ROLE_DB_TABLE.ID)
		                              .from(AUTH_USER_ROLE_DB_TABLE)
		                              .where(AUTH_USER_ROLE_DB_TABLE.CODE.eq(authUserRoleCode))
		                              .fetchOptional(AUTH_USER_ROLE_DB_TABLE.ID)
		                              .orElseThrow(() -> new EntityNotFoundException(
				                                    "No existing auth user role found for [code=%s]".formatted(
						                                    authUserRoleCode)));

		return db.insertInto(AUTH_USER_DB_TABLE)
		         .set(AUTH_USER_DB_TABLE.EMAIL, authUser.getEmail())
		         .set(AUTH_USER_DB_TABLE.PASSWORD, authUser.getPassword())
		         .set(AUTH_USER_DB_TABLE.FIRST_NAME, authUser.getFirstName())
		         .set(AUTH_USER_DB_TABLE.LAST_NAME, authUser.getLastName())
		         .set(AUTH_USER_DB_TABLE.ROLE_ID, authUserRoleId)
		         .returning(AUTH_USER_DB_TABLE.ID)
		         .fetchOne(record -> AuthUser.builder()
		                                     .id(record.getValue(AUTH_USER_DB_TABLE.ID))
		                                     .build());
	}

	public AuthUser authUser(final String authUserEmail) {
		return this.authUser(AUTH_USER_DB_TABLE.EMAIL.eq(authUserEmail),
				() -> new EntityNotFoundException("No existing auth user found for [email=%s]"
						.formatted(authUserEmail)));
	}

	public AuthUser authUser(final Long userId) {
		return this.authUser(AUTH_USER_DB_TABLE.ID.eq(userId),
				() -> new EntityNotFoundException("No existing auth user found for [id=%d]".formatted(userId)));
	}

	public void deleteAuthUser(final Long authUserId) {
		db.deleteFrom(AUTH_USER_DB_TABLE)
		  .where(AUTH_USER_DB_TABLE.ID.eq(authUserId))
		  .execute();
	}

	public void assertAuthUserNotExists(final String email) {
		boolean flowExists =
				db.fetchExists(
						db.selectOne()
						  .from(AUTH_USER_DB_TABLE)
						  .where(AUTH_USER_DB_TABLE.EMAIL.eq(email)));

		if (flowExists) {
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
						         ).from(AUTH_USER_ROLE_DB_TABLE)
						          .where(AUTH_USER_ROLE_DB_TABLE.ID.eq(AUTH_USER_DB_TABLE.ROLE_ID))
				         ).as("stepGroups")
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
