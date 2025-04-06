package org.project.we3.di

import android.app.Application
import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.project.we3.data.local.CameraDatabase
import org.project.we3.data.local.CameraLocalRepository
import org.project.we3.data.repository.AdminAuth
import org.project.we3.data.repository.AdminAuthImpl
import org.project.we3.data.repository.CameraRepository
import org.project.we3.data.datsource.CameraRepositoryImpl
import org.project.we3.data.datsource.EarningRepositoryImpl
import org.project.we3.data.datsource.QuotationRepositoryImpl
import org.project.we3.data.repository.EarningRepository
import org.project.we3.data.repository.QuotationRepository
import org.project.we3.domain.usecase.CalculateTotalPriceUseCase
import org.project.we3.domain.usecase.GeneratePDFUseCase
import org.project.we3.domain.usecase.camera.AddCameraUseCase
import org.project.we3.domain.usecase.camera.RemoveCameraUseCase
import org.project.we3.domain.usecase.camera.UpdateCameraQuantityUseCase
import org.project.we3.domain.usecase.camera.UpdateCameraUseCase
import org.project.we3.domain.usecase.earning.AddEarningUseCase
import org.project.we3.domain.usecase.earning.FetchEarningUseCase
import org.project.we3.domain.usecase.quotation.FetchQuotationUseCase
import org.project.we3.domain.usecase.quotation.FetchSpecificQuotationUseCase
import org.project.we3.domain.usecase.earning.RemoveEarningUseCase
import org.project.we3.domain.usecase.quotation.RemoveQuotationUseCase
import org.project.we3.domain.usecase.camera.SortAndFilterCameraUseCase
import org.project.we3.domain.usecase.earning.SortEarningByDateUseCase
import org.project.we3.domain.usecase.quotation.SortAndFilterQuotationUseCase
import org.project.we3.domain.usecase.quotation.ToggleActiveStatusUseCase
import org.project.we3.domain.usecase.earning.UpdateEarningUseCase
import org.project.we3.domain.usecase.quotation.AddQuotationUseCase
import org.project.we3.domain.usecase.quotation.UpdateQuotationUseCase
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
    fun provideCameraFirebaseRepository(firestore: FirebaseFirestore): CameraRepository {
        return CameraRepositoryImpl(firestore)
    }
    @Provides
    @Singleton
    fun provideCameraRepository(app: Application): CameraLocalRepository {
        return CameraLocalRepository(CameraDatabase.Companion.getDatabase(app).cameraDao())
    }

    @Provides
    @Singleton
    fun provideAdminAuth(firestore: FirebaseFirestore): AdminAuth {
        return AdminAuthImpl(firestore)
    }
    @Provides
    @Singleton
    fun provideQuotationRepository(firestore: FirebaseFirestore): QuotationRepository {
        return QuotationRepositoryImpl(firestore)
    }
    @Provides
    @Singleton
    fun provideEarningRepository(firestore: FirebaseFirestore): EarningRepository {
        return EarningRepositoryImpl(firestore)
    }
    @Provides
    @Singleton
    fun provideSortAndFilterCameraUseCase(): SortAndFilterCameraUseCase {
        return SortAndFilterCameraUseCase()
    }
    @Provides
    @Singleton
    fun provideFetchQuotationUseCase(quotationRepository: QuotationRepository, sortAndFilterQuotationUseCase: SortAndFilterQuotationUseCase): FetchQuotationUseCase{
        return FetchQuotationUseCase(quotationRepository, sortAndFilterQuotationUseCase)
    }
    @Provides
    @Singleton
    fun provideRemoveQuotationUseCase(quotationRepository: QuotationRepository, cameraRepository: CameraRepository): RemoveQuotationUseCase {
        return RemoveQuotationUseCase(quotationRepository, cameraRepository)
    }
    @Provides
    @Singleton
    fun provideToggleActiveStatusUseCase(quotationRepository: QuotationRepository): ToggleActiveStatusUseCase {
        return ToggleActiveStatusUseCase(quotationRepository)
    }
    @Provides
    @Singleton
    fun provideUpdateEarningUseCase(earningRepository: EarningRepository, calculateTotalPriceUseCase: CalculateTotalPriceUseCase): UpdateEarningUseCase {
        return UpdateEarningUseCase(earningRepository, calculateTotalPriceUseCase)
    }
    @Provides
    @Singleton
    fun provideCalculateTotalPriceUseCase(): CalculateTotalPriceUseCase {
        return CalculateTotalPriceUseCase()
    }
    @Provides
    @Singleton
    fun provideSortAndFilterQuotationUseCase(): SortAndFilterQuotationUseCase {
        return SortAndFilterQuotationUseCase()
    }
    @Provides
    @Singleton
    fun provideUpdateQuotationUseCase(quotationRepository: QuotationRepository): UpdateQuotationUseCase {
        return UpdateQuotationUseCase(quotationRepository)
    }
    @Provides
    @Singleton
    fun provideAddQuotationUseCase(quotationRepository: QuotationRepository): AddQuotationUseCase {
        return AddQuotationUseCase(quotationRepository)
    }
    @Provides
    @Singleton
    fun provideAddEarningUseCase(earningRepository: EarningRepository): AddEarningUseCase {
        return AddEarningUseCase(earningRepository)
    }
    @Provides
    @Singleton
    fun provideFetchEarningUseCase(earningRepository: EarningRepository, sortEarningByDateUseCase: SortEarningByDateUseCase): FetchEarningUseCase {
        return FetchEarningUseCase(earningRepository, sortEarningByDateUseCase)
    }

    @Provides
    @Singleton
    fun provideRemoveEarningUseCase(earningRepository: EarningRepository): RemoveEarningUseCase {
        return RemoveEarningUseCase(earningRepository)
    }

    @Provides
    @Singleton
    fun provideFetchSpecificQuotationUseCase(quotationRepository: QuotationRepository): FetchSpecificQuotationUseCase {
        return FetchSpecificQuotationUseCase(quotationRepository)
    }
    @Provides
    @Singleton
    fun provideAddCameraUseCase(cameraRepository: CameraRepository): AddCameraUseCase {
        return AddCameraUseCase(cameraRepository)
    }
    @Provides
    @Singleton
    fun provideUpdateCameraUseCase(cameraRepository: CameraRepository): UpdateCameraUseCase {
        return UpdateCameraUseCase(cameraRepository)
    }
    @Provides
    @Singleton
    fun provideRemoveCameraUseCase(cameraRepository: CameraRepository): RemoveCameraUseCase {
        return RemoveCameraUseCase(cameraRepository)
    }
    @Provides
    @Singleton
    fun provideUpdateCameraQuantityUseCase(cameraRepository: CameraRepository): UpdateCameraQuantityUseCase {
        return UpdateCameraQuantityUseCase(cameraRepository)
    }

    @Provides
    @Singleton
    fun provideSortEarningByDateUseCase(): SortEarningByDateUseCase {
        return SortEarningByDateUseCase()
    }
    @Provides
    @Singleton
    fun provideGeneratePDFUseCase(quotationRepository: QuotationRepository, cameraRepository: CameraRepository): GeneratePDFUseCase{
        return GeneratePDFUseCase(quotationRepository, cameraRepository)
    }
}