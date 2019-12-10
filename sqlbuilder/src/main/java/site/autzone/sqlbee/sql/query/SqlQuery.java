package site.autzone.sqlbee.sql.query;

import site.autzone.sqlbee.query.AbstractQuery;

public class SqlQuery extends AbstractQuery {
	private boolean isMySql = false;
	public void isMySql(boolean isMySql) {
		this.isMySql = isMySql;
	}
	
	public boolean isMySql() {
		return this.isMySql;
	}
	@Override
	public String buildCloumns() {
		if(super.isCount()) {
			if(super.getCloumns().size() == 0) {
				return "COUNT(*) COUNT";
			}else {
				StringBuilder countField = new StringBuilder();
				if(super.getCloumns().get(0).getPrefix() != null) {
					countField.append(super.getCloumns().get(0).getPrefix()).append(".");
				}
				countField.append(super.getCloumns().get(0).getName());
				return "COUNT("+countField.toString()+") COUNT";
			}
		}
		
		if(super.getCloumns().size() == 0) {
			if(super.getQueryObjects().size() == 1) {
				String queryObjectAlias = 
						super.getQueryObjects()
						.get(0)
						.getAlias();
				return queryObjectAlias == null?"*":queryObjectAlias+".*";
			}else {
				throw new RuntimeException("Queries that are larger than one table, Need to specify the query column!");
			}
		}else {
			return QueryUtils.joinTextableWithStr(super.getCloumns(), ",");
		}
	}

	@Override
	public String buildQueryObjects() {
		return QueryUtils.joinTextableWithStr(super.getQueryObjects(), ",");
	}

	@Override
	public String buildConditions() {
		String condition = QueryUtils.joinTextableWithStr(super.getConditions(), " AND ");
		if(condition.length() > 0) {
			return " WHERE "+condition;
		}else {
			return condition;
		}
	}
	
	@Override
	public String buildQueryText() {
		StringBuffer sqlLimit = new StringBuffer();
		if(this.isCount()) {
			if(this.isMySql()) {
				if(super.getMaxResultsKey() != null) {
					sqlLimit.append(super.buildQueryText()).append(" LIMIT ").append(super.getMaxResultsKey());
					return sqlLimit.toString();
				}
			}else {
				if(super.getMaxResultsKey() != null) {
					sqlLimit.append(super.buildQueryText());
					if(super.getConditions().size() == 0) {
						sqlLimit.append(" WHERE ROWNUM <= ").append(super.getMaxResultsKey());
					}else {
						sqlLimit.append(" AND ROWNUM <= ").append(super.getMaxResultsKey());
					}
					return sqlLimit.toString();
				}
			}
		}else {
			if(this.isMySql()) {
				if(super.getMaxResultsKey() != null) {
					if(super.getMaxResultsKey() != null && super.getFirstResultKey() != null) {
						sqlLimit.append(super.buildQueryText())
							.append(" LIMIT ")
							.append(super.getFirstResultKey()).append(",")
							.append(super.getMaxResultsKey());
						return sqlLimit.toString();
					}
					if(super.getFirstResultKey() != null) {
						sqlLimit.append(super.buildQueryText())
							.append(" LIMIT ")
							.append(super.getFirstResultKey()).append(",")
							.append(super.getMaxResultsKey());
						return sqlLimit.toString();
					}
					if(super.getMaxResultsKey() != null) {
						sqlLimit.append(super.buildQueryText()).append(" LIMIT ").append(super.getMaxResultsKey());
						return sqlLimit.toString();
					}
				}
			}else {
				if(super.getMaxResultsKey() != null && super.getFirstResultKey() != null) {
					sqlLimit.append("SELECT * FROM (SELECT A.*, ROWNUM RNUM FROM (")
					.append(super.buildQueryText())
					.append(") A WHERE ROWNUM <= ").append(super.getMaxResultsKey())
					.append(") WHERE RNUM >= ")
					.append(super.getFirstResultKey());
					
					return sqlLimit.toString();
				}
				if(super.getFirstResultKey() != null) {
					sqlLimit.append("SELECT * FROM (SELECT A.*, ROWNUM RNUM FROM (")
					.append(super.buildQueryText())
					.append(") A")
					.append(") WHERE A.RNUM >= ")
					.append(super.getFirstResultKey());
					
					return sqlLimit.toString();
				}
				
				if(super.getMaxResultsKey() != null) {
					sqlLimit.append("SELECT A.*, ROWNUM RNUM FROM (")
					.append(super.buildQueryText())
					.append(") A WHERE ROWNUM < ")
					.append(super.getMaxResultsKey());
					
					return sqlLimit.toString();
				}
			}
		}
		return super.buildQueryText();
	}
}
