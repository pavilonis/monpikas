package lt.pavilonis.cmm.api.rest.user;

import lt.pavilonis.cmm.common.util.QueryUtils;
import lt.pavilonis.cmm.school.user.UserFilter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

@Repository
public class UserRepository {

   private static final String BLOCKS_FROM_WHERE = "" +
         "FROM User " +
         "WHERE " +
         "  cardCode IS NOT NULL " +
         "  AND (:name IS NULL OR name LIKE :name) " +
         "  AND (:role IS NULL OR organizationRole LIKE :role) " +
         "  AND (:group IS NULL OR organizationGroup LIKE :group)" +
         "  AND (:withFirstLastNameOnly = '0' OR (name IS NOT NULL AND name <> ''))";

   public static final String SELECT_FROM_USER = "SELECT " +
         "  id, " +
         "  cardCode, " +
         "  name, " +
         "  birthDate, " +
         "  organizationGroup, " +
         "  organizationRole," +
         "  CASE WHEN :withPhoto = 1 " +
         "     THEN " +
         "        picture " +
         "     ELSE " +
         "        NULL " +
         "  END AS photo " +
         "FROM User ";

   private final NamedParameterJdbcTemplate jdbc;
   private final Logger logger = getLogger(getClass());

   public UserRepository(NamedParameterJdbcTemplate jdbc) {
      this.jdbc = jdbc;
   }

   public User update(User user) {
      var sql = "UPDATE User " +
            "SET " +
            "  name = :name, " +
            "  birthDate = :birthDate, " +
            "  organizationGroup = :group, " +
            "  organizationRole = :role," +
            "  picture = :base16photo " +
            "WHERE id = :id";

      jdbc.update(sql, collectArgs(user));
      return load(user.getId(), false);
   }

   public List<User> load(UserFilter filter) {

      Map<String, Object> args = commonArgs(filter);
      args.put("argOffset", QueryUtils.argOffset(filter.getOffset()));
      args.put("argLimit", QueryUtils.argLimit(filter.getLimit()));

      var sql = "SELECT " +
            "  id, " +
            "  cardCode, " +
            "  name, " +
            "  birthDate, " +
            "  organizationGroup, " +
            "  organizationRole," +
            "  NULL AS photo " + // Photos are only loaded for single user requests
            BLOCKS_FROM_WHERE +
            "ORDER BY name ASC " +
            "LIMIT :argLimit OFFSET :argOffset";

      return jdbc.query(sql, args, new UserMapper());
   }

   protected Map<String, Object> commonArgs(UserFilter filter) {
      Map<String, Object> args = new HashMap<>();
      args.put("name", QueryUtils.likeArg(filter.getName()));
      args.put("role", filter.getRole());
      args.put("group", filter.getGroup());
      args.put("withFirstLastNameOnly", filter.isWithFirstLastNameOnly());
      return args;
   }

   public User load(long id, boolean withPhoto) {
      var params = Map.of("id", id, "withPhoto", withPhoto);
      List<User> result = jdbc.query(SELECT_FROM_USER + "WHERE id = :id", params, new UserMapper());

      if (result.isEmpty()) {
         logger.warn("User not found - returning NULL [id={}]", id);
         return null;
      } else {
         return result.get(0);
      }
   }

   public User load(String cardCode, boolean withPhoto) {
      var params = Map.of("cardCode", cardCode, "withPhoto", withPhoto);
      List<User> result = jdbc.query(SELECT_FROM_USER + "WHERE cardCode = :cardCode ", params, new UserMapper());

      if (result.isEmpty()) {
         logger.warn("User not found - returning NULL [cardCode={}]", cardCode);
         return null;
      } else {
         return result.get(0);
      }
   }

   private Map<String, Object> collectArgs(User user) {

      String photo = StringUtils.isNotBlank(user.getBase16photo())
            ? user.getBase16photo()
            : null;

      var params = new HashMap<String, Object>();
      params.put("id", user.getId());
      params.put("cardCode", user.getCardCode());
      params.put("name", user.getName());
      params.put("group", user.getOrganizationGroup());
      params.put("role", user.getOrganizationRole());
      params.put("birthDate", user.getBirthDate());
      params.put("base16photo", photo);
      params.put("now", LocalDateTime.now());
      return params;
   }

   public boolean exists(String cardCode) {
      return jdbc.queryForObject("" +
                  "SELECT " +
                  "  CASE " +
                  "     WHEN COUNT(cardCode) > 0 " +
                  "     THEN 1 " +
                  "     ELSE 0 " +
                  "  END " +
                  "FROM User " +
                  "WHERE cardCode IS NOT NULL AND cardCode = :cardCode",
            Map.of("cardCode", cardCode),
            Boolean.class
      );
   }

   public int size(UserFilter filter) {
      return jdbc.queryForObject(
            "SELECT COUNT(cardCode) " + BLOCKS_FROM_WHERE,
            commonArgs(filter),
            Integer.class
      );
   }

   public List<String> loadGroups() {
      String sql = "SELECT DISTINCT organizationGroup " +
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

   public void delete(long id) {
      jdbc.update("DELETE FROM User WHERE id = :id", Map.of("id", id));
   }

   public User create(User entity) {
      var sql = "INSERT INTO User (name, cardCode, birthDate, organizationRole, organizationGroup, picture)\n" +
            "VALUES (:name, :cardCode, :birthDate, :role, :group, :picture)";
      var params = new HashMap<String, Object>();

      params.put("name", entity.getName());
      params.put("cardCode", entity.getCardCode());
      params.put("birthDate", entity.getBirthDate());
      params.put("role", entity.getOrganizationRole());
      params.put("group", entity.getOrganizationGroup());
      params.put("picture", entity.getBase16photo());

      jdbc.update(sql, params);
      return load(entity.getCardCode(), false);
   }
}
