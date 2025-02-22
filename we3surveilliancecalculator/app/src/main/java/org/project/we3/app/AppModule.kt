package org.project.we3.app

import android.app.Application
import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.project.we3.app.db.CameraDatabase
import org.project.we3.app.db.CameraRepository
import org.project.we3.app.repository.FirestoreDBRepository
import org.project.we3.app.repository.FirestoreDBRepositoryImpl
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore {
        return Firebase.firestore
    }
    @Provides
    @Singleton
    fun provideApplicationContext(app: Application): Context {
        return app.applicationContext
    }
    @Provides
    @Singleton
    fun provideFirestoreDBRepository(firestore: FirebaseFirestore): FirestoreDBRepository {
        return FirestoreDBRepositoryImpl(firestore)
    }
    @Provides
    @Singleton
    fun provideCameraRepository(app: Application): CameraRepository {
        return CameraRepository(CameraDatabase.getDatabase(app).cameraDao())
    }

}