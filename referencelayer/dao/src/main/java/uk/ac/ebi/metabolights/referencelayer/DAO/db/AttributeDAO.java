package uk.ac.ebi.metabolights.referencelayer.DAO.db;


import uk.ac.ebi.metabolights.referencelayer.IDAO.DAOException;
import uk.ac.ebi.metabolights.referencelayer.IDAO.IAttributeDAO;
import uk.ac.ebi.metabolights.referencelayer.domain.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public class AttributeDAO extends AbstractDAO implements IAttributeDAO{

    private AttributeDefinitionDAO add;


	/**
	 * @param connection to the AttributeDAO
	 * @throws java.io.IOException
	 */
	public AttributeDAO(Connection connection) throws IOException{
		super(connection);
        setUp(this.getClass());

        this.add = new AttributeDefinitionDAO(connection);

	}


    /**
     * Setter for Attribute connection. It also sets the same connection
     * for the underlying objects.<br>
     * This method should be used with pooled connections, and only when the
     * previous one and its prepared statements have been properly closed
     * (returned).
     * @param con
     * @throws java.sql.SQLException
     */
	public void setConnection(Connection con) throws SQLException{
		this.setConnection(con);
		this.add.setConnection(con);
	}

	public Collection<Attribute> findBySpectraId(Long spectraId) throws DAOException {

       // It must return an array of Attribute....
       Collection<Attribute> attributes = findBy("--where.attribute.by.spectraid", spectraId);

       return attributes;
	}

	private Collection <Attribute> findBy(String where, Object value)
	throws DAOException {
		ResultSet rs = null;
		try {

			PreparedStatement stm = sqlLoader.getPreparedStatement("--attribute.core", where);
			stm.clearParameters();
			
			// If can be casted as long
			if (value instanceof Long){
				stm.setLong(1, (Long) value);
			} else {
				stm.setString(1, (String) value);
			}

			rs = stm.executeQuery();
			
			// Load all attribute
			return loadAttributes(rs);
			
        } catch (SQLException e){
           throw new DAOException(e);
		} finally {
			if (rs != null) try {
                rs.close();
            } catch (SQLException ex) {
                LOGGER.error("Closing ResultSet", ex);
            }
		}
	}
	
	private Collection<Attribute> loadAttributes(ResultSet rs) throws SQLException, DAOException {
		
		Set<Attribute> result = new HashSet<Attribute>();

        while (rs.next()){

			Attribute attributes = loadAttribute(rs);
			result.add(attributes);
		}
		return result;
		
	}

	private Attribute loadAttribute(ResultSet rs) throws SQLException, DAOException {


        long id = rs.getLong("ID");
		long spectra_id = rs.getLong("SPECTRA_ID");
        long attribute_def_id = rs.getLong("ATTRIBUTE_DEF_ID");
		String value = rs.getString("VALUE");

        // Get the referenced objects
        AttributeDefinition ad = add.findByAttributeDefinitionId(attribute_def_id);


        Attribute attribute = new Attribute();
        attribute.setValue(value);
        attribute.setId(id);
        attribute.setAttributeDefinition(ad);

		return attribute;
	}

	public void saveSpectraAttribute(Attribute attribute, Spectra spectra) throws DAOException {

        // Validate:
        // Attribute definition must exist
        if (attribute.getAttributeDefinition() == null ){
            String msg = "Attribute can't be saved without an attributeDefiniton object associated";
            LOGGER.error(msg);
            throw new DAOException(msg);
        }

        // Spectra must exist
        if (spectra == null ){
            String msg = "Attribute can't be saved without as Spectra object associated";
            LOGGER.error(msg);
            throw new DAOException(msg);
        }


        // Before saving the Attribute data we need to save the foreign key entities if apply
        // We are assuming the Spectra it's been saved and the Spectra DAO is the one calling this method
        if (attribute.getAttributeDefinition().getId() == 0) add.save(attribute.getAttributeDefinition());

		// If its a new attribute
		if (attribute.getId() == 0) {
			insert (attribute,spectra);
		} else {
			update(attribute,spectra);
		}
		
	}
	
	/**
	 * Updates core data concerning only to the Attribute
	 * @param attribute
	 * @throws uk.ac.ebi.metabolights.referencelayer.IDAO.DAOException
	 */
	private void update(Attribute attribute, Spectra spectra ) throws DAOException {
		try {
		
			PreparedStatement stm = sqlLoader.getPreparedStatement("--update.attribute");
			stm.clearParameters();
            stm.setLong(1, attribute.getAttributeDefinition().getId());
            stm.setLong(2, spectra.getId());
            stm.setString(3, attribute.getValue());
            stm.setLong(4, attribute.getId());

			stm.executeUpdate();
	
		} catch (SQLException ex) {
            throw new DAOException(ex);
		}
	}
	
	/**
	 * Inserts a new Attribute into the Attributes table
	 * <br>
	 * @throws java.sql.SQLException
	 */
	private void insert(Attribute attribute, Spectra spectra) throws DAOException {
		try {
			PreparedStatement stm = sqlLoader.getPreparedStatement("--insert.attribute", new String[]{"ID"}, null);
			stm.clearParameters();
            stm.setLong(1, attribute.getAttributeDefinition().getId());
            stm.setLong(2, spectra.getId());
            stm.setString(3, attribute.getValue());
			stm.executeUpdate();
	
			ResultSet keys = stm.getGeneratedKeys();
			
       		while (keys.next()) {
    			attribute.setId(keys.getLong(1));  //Should only be one
    	        if (LOGGER.isDebugEnabled())
    	            LOGGER.debug("insertIntoAttribute: attribute inserted with id:" + attribute.getId());
    		}
    		
       		keys.close();

       		
		} catch (SQLException ex) {
            throw new DAOException(ex);
		}
	}
	
	/**
	 * Deletes an Attribute from the Attributes table (and its dependants)
	 * <br>
	 * @throws java.sql.SQLException
	 */
	public void delete(Attribute attribute) throws DAOException {
		
		// Delete the Attribute
		deleteAttribute(attribute);
	}

	/**
	 * Deletes an Attribute from the Attributes table
	 * <br>
	 * @throws java.sql.SQLException
	 */
	private void deleteAttribute(Attribute attribute)	throws DAOException {
		try {
			PreparedStatement stm = sqlLoader.getPreparedStatement("--delete.attribute", "--where.attribute.by.id");
			stm.clearParameters();
			stm.setLong(1, attribute.getId());
			stm.executeUpdate();
	
	        if (LOGGER.isDebugEnabled())
    	            LOGGER.debug("Attribute deleted with id:" + attribute.getId());
    		

		} catch (SQLException ex) {
            throw new DAOException(ex);
		}
	}
}