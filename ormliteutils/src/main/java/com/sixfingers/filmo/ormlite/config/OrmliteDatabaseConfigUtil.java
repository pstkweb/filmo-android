package com.sixfingers.filmo.ormlite.config;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;
import com.sixfingers.filmo.model.Collection;
import com.sixfingers.filmo.model.CollectionMovie;
import com.sixfingers.filmo.model.Movie;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class OrmliteDatabaseConfigUtil extends OrmLiteConfigUtil {
    private static final Class<?>[] classes = new Class[]{
            Movie.class, Collection.class, CollectionMovie.class
    };

    public static void main(String[] args) throws IOException, SQLException {
        String curDirectory = "user.dir";
        String configPath = "/app/src/main/res/raw/ormlite_config.txt";
        String projectRoot = System.getProperty(curDirectory);
        String fullConfigPath = projectRoot + configPath;
        File configFile = new File(fullConfigPath);

        if (configFile.exists()) {
            configFile.delete();
            configFile = new File(fullConfigPath);
        }

        writeConfigFile(configFile, classes);
    }
}
