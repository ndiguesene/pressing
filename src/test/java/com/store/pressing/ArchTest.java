package com.store.pressing;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.store.pressing");

        noClasses()
            .that()
            .resideInAnyPackage("com.store.pressing.service..")
            .or()
            .resideInAnyPackage("com.store.pressing.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..com.store.pressing.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
