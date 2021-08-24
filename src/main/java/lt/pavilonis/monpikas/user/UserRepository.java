package lt.pavilonis.monpikas.user;

import lt.pavilonis.monpikas.common.util.QueryUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.time.LocalDateTime.now;
import static lt.pavilonis.monpikas.common.util.TimeUtils.duration;

@Repository
public class UserRepository {

   public static final UserMapper USER_MAPPER = new UserMapper();
   private static final Logger LOGGER = LoggerFactory.getLogger(UserRepository.class);
   private static final String BLOCK_WHERE = " WHERE (:name IS NULL OR u.name LIKE :name)\n" +
         "  AND (:role IS NULL OR u.organizationRole LIKE :role)\n" +
         "  AND (:group IS NULL OR u.organizationGroup LIKE :group)\n";

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
      LOGGER.info("Loaded users [size={}, t={}]", result.size(), duration(start));
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
         LOGGER.warn("User not found [id={}, cardCode={}]", id, cardCode);
         return null;
      }
      LOGGER.info("Loaded user [userId={}, cardCode={}, t={}]", id, cardCode, duration(start));
      return result.get(0);
   }

   private Map<String, Object> collectParams(User user) {
      Long supervisorId = user.getSupervisor() == null ? null : user.getSupervisor().getId();
      String photo = StringUtils.hasText(user.getBase64photo()) ? user.getBase64photo() : null;

      var params = new HashMap<String, Object>();
      params.put("id", user.getId());
      params.put("cardCode", user.getCardCode());
      params.put("name", user.getName());
      params.put("group", user.getOrganizationGroup());
      params.put("role", user.getOrganizationRole());
      params.put("birthDate", user.getBirthDate());
      params.put("supervisorId", supervisorId);
      params.put("photo", photo);
      params.put("now", now());
      return params;
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
            "VALUES (:name, :cardCode, :birthDate, :role, :group, :supervisorId, :photo)";

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
            "  cardCode = :cardCode," +
            "  photo = :photo " +
            "WHERE id = :id";

      jdbc.update(sql, collectParams(user));
      return load(user.getId(), false);
   }

   public void delete(long id) {
      User userToDelete = load(id, false);
      jdbc.update("DELETE FROM User WHERE id = :id", Map.of("id", id));
      LOGGER.debug("User deleted: {}", userToDelete);
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
