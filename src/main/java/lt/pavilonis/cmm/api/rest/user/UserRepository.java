package lt.pavilonis.cmm.api.rest.user;

import lt.pavilonis.cmm.common.util.QueryUtils;
import lt.pavilonis.cmm.school.user.UserFilter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Boolean.TRUE;
import static java.time.LocalDateTime.now;
import static lt.pavilonis.cmm.common.util.TimeUtils.duration;

@Repository
public class UserRepository {

   public static final UserMapper USER_MAPPER = new UserMapper();
   private static final String BLOCK_WHERE = " WHERE (:name IS NULL OR u.name LIKE :name)\n" +
         "  AND (:role IS NULL OR u.organizationRole LIKE :role)\n" +
         "  AND (:group IS NULL OR u.organizationGroup LIKE :group)\n";

   private final Logger logger = LoggerFactory.getLogger(getClass());
   private final NamedParameterJdbcTemplate jdbc;

   public UserRepository(NamedParameterJdbcTemplate jdbc) {
      this.jdbc = jdbc;
   }

   public List<User> load(UserFilter filter) {
      var start = now();
      Map<String, Object> params = commonParams(filter);
      params.put("argOffset", QueryUtils.argOffset(filter.getOffset()));
      params.put("argLimit", QueryUtils.argLimit(filter.getLimit()));

      var sql = selectUser(false) + BLOCK_WHERE + " ORDER BY u.name ASC LIMIT :argLimit OFFSET :argOffset";
      List<User> result = jdbc.query(sql, params, USER_MAPPER);
      logger.info("Loaded users [size={}, t={}]", result.size(), duration(start));
      return result;
   }

   private Map<String, Object> commonParams(UserFilter filter) {
      var params = new HashMap<String, Object>();
      params.put("name", QueryUtils.likeArg(filter.getName()));
      params.put("role", filter.getRole());
      params.put("group", filter.getGroup());
      return params;
   }

   public User load(long id, boolean withPhoto) {
      return load(id, null, withPhoto);
   }

   public User load(String cardCode, boolean withPhoto) {
      return load(null, cardCode, withPhoto);
   }

   private User load(Long id, String cardCode, boolean withPhoto) {
      var start = now();
      var params = new HashMap<String, Object>();
      params.put("id", id);
      params.put("cardCode", cardCode);

      var sql = selectUser(withPhoto) + "\nWHERE " + (id == null ? "u.cardCode = :cardCode " : "u.id = :id");
      List<User> result = jdbc.query(sql, params, USER_MAPPER);

      if (result.isEmpty()) {
         logger.warn("User not found - returning NULL [id={}, cardCode={}]", id, cardCode);
         return null;
      } else {
         logger.info("Loaded user [userId={}, cardCode={}, t={}]", id, cardCode, duration(start));
         return result.get(0);
      }
   }

   private Map<String, Object> collectParams(User user) {
      Long supervisorId = user.getSupervisor() == null ? null : user.getSupervisor().getId();
      String photo = StringUtils.isNotBlank(user.getBase16photo()) ? user.getBase16photo() : null;

      var params = new HashMap<String, Object>();
      params.put("id", user.getId());
      params.put("cardCode", user.getCardCode());
      params.put("name", user.getName());
      params.put("group", user.getOrganizationGroup());
      params.put("role", user.getOrganizationRole());
      params.put("birthDate", user.getBirthDate());
      params.put("supervisorId", supervisorId);
      params.put("base16photo", photo);
      params.put("now", now());
      return params;
   }

   public boolean exists(String cardCode) {
      var sql = "SELECT 1 FROM User WHERE cardCode = :cardCode";
      return TRUE.equals(jdbc.queryForObject(sql, Map.of("cardCode", cardCode), Boolean.class));
   }

   public int size(UserFilter filter) {
      var sql = "SELECT COUNT(u.cardCode) FROM User u " + BLOCK_WHERE;
      return jdbc.queryForObject(sql, commonParams(filter), Integer.class);
   }

   public List<String> loadGroups() {
      var sql = "SELECT DISTINCT organizationGroup " +
            "FROM User " +
            "WHERE organizationGroup IS NOT NULL " +
            "  AND organizationGroup <> '' " +
            "ORDER BY organizationGroup ASC";
      return jdbc.queryForList(sql, Map.of(), String.class);
   }

   public List<String> loadRoles() {
      var sql = "SELECT DISTINCT organizationRole " +
            "FROM User " +
            "WHERE organizationRole IS NOT NULL " +
            "  AND organizationRole <> '' " +
            "ORDER BY organizationRole ASC";
      return jdbc.queryForList(sql, Map.of(), String.class);
   }

   public User create(User entity) {
      var sql = "INSERT INTO User (name, cardCode, birthDate, " +
            "  organizationRole, organizationGroup, supervisor_id, photo)\n" +
            "VALUES (:name, :cardCode, :birthDate, :role, :group, :photo)";

      var keyHolder = new GeneratedKeyHolder();
      jdbc.update(sql, new MapSqlParameterSource(collectParams(entity)), keyHolder);
      return load(keyHolder.getKey().longValue(), false);
   }

   public User update(User user) {
      var sql = "UPDATE User " +
            "SET " +
            "  name = :name, " +
            "  birthDate = :birthDate, " +
            "  organizationGroup = :group, " +
            "  organizationRole = :role," +
            "  supervisor_id = :supervisorId," +
            "  photo = :base16photo " +
            "WHERE id = :id";

      jdbc.update(sql, collectParams(user));
      return load(user.getId(), false);
   }

   public void delete(long id) {
      User userToDelete = load(id, false);
      jdbc.update("DELETE FROM User WHERE id = :id", Map.of("id", id));
      logger.debug("User deleted: {}", userToDelete);
   }

   private String selectUser(boolean withPhoto) {
      return "SELECT " +
            "  u.id, " +
            "  u.cardCode, " +
            "  u.name, " +
            "  u.birthDate, " +
            "  u.organizationGroup, " +
            "  u.organizationRole, " +
            "  supervisor.id AS supervisorId, " +
            "  supervisor.name AS supervisorName, " +
            (withPhoto ? "u.photo\n" : "NULL AS photo\n") +
            " FROM User u\n" +
            "  LEFT JOIN User supervisor ON supervisor.id = u.supervisor_id \n";
   }
}
