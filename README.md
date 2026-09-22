# Lab SCA multi-modules — vulnerable-app

Projet Maven multi-modules **volontairement vulnerable**, concu pour s'entrainer
au **workflow de triage / remediation SCA** dans Checkmarx One.
**Ne jamais utiliser en production.**

## Structure

```
vulnerable-app/            (parent, packaging=pom)
├── app-api/               couche REST
├── app-core/              logique metier
├── app-batch/             traitements batch
└── app-integrations/      services externes (SaaS)
```

Chaque module a son propre `pom.xml` → tu t'entraines sur un vrai projet
multi-poms, comme en mission.

## Avant de scanner

```bash
mvn -N dependency:resolve   # optionnel, verifie la resolution
```
Puis pousse sur Git et scanne via Checkmarx One (SCA + SAST).
La dependance interne `com.bpce.internal:legacy-crypto` ne se resout pas
publiquement (c'est voulu) — elle est en `optional` pour ne pas casser le build.

---

## Carte des scenarios de triage (corrige — a ne regarder qu'apres ton analyse)

Le but est d'appliquer le workflow : qualifier chaque finding avant de corriger.

| Package | Module | Type | Usage | Scenario de triage attendu |
|---|---|---|---|---|
| jackson-databind 2.9.8 | app-api | Direct | **Used** (desertialise une entree utilisateur) | **Vrai positif Critical** → upgrade prioritaire (2.17.x) |
| spring-web 5.2.0 | app-api | Direct | Used | Vrai positif → upgrade (tire spring-beans/core en transitif) |
| spring-beans / spring-core | app-api | **Transitive** | — | Se corrigent en montant spring-web |
| commons-fileupload 1.3.1 | app-api | Direct | **Unused** | Dependance morte → **supprimer** (ou faux positif de risque) |
| log4j-core 2.14.1 | app-core | Direct | **Used** (log d'entree) | **Vrai positif Critical (Log4Shell)** → upgrade urgent (2.24.x) |
| snakeyaml 1.30 | app-core | Direct | Used | Vrai positif → upgrade (2.x) |
| commons-collections 3.2.1 | app-core | Direct | **Unused** | Non exploitable → **Not Exploitable** ou suppression |
| guava 24.1-jre | app-core | Direct | **Unused** | Faible priorite → upgrade d'hygiene ou suppression |
| junit 4.12 | app-core | Direct (**test**) | Test | Dependance Dev/Test → priorite basse (n'atteint pas la prod) |
| mysql-connector 8.0.11 | app-batch | Direct | Used | Vrai positif → upgrade (8.0.33+) |
| xstream 1.4.5 | app-batch | Direct | **Used** | Vrai positif mais upgrade = **changement majeur cassant** → **risque accepte** documente, ou mitigation (allowlist des types) |
| legacy-crypto 1.0.0 | app-batch | **Private** | Unused | Artefact interne non identifie → traiter hors SCA public (revue interne) |
| stripe-java 20.0.0 | app-integrations | Direct (**SaaS**) | Used | Vrai positif → upgrade |
| httpclient 4.5.1 | app-integrations | Direct | Used | Vrai positif → upgrade (4.5.13+) |

### Les 3 statuts de triage a manipuler dans Checkmarx
- **A corriger / Confirmed** : vrai positif exploitable (jackson, log4j, snakeyaml…).
- **Not Exploitable / Proposed Not Exploitable** : faux positif ou dep. non utilisee (commons-collections Unused).
- **Risque accepte (accepted)** : vrai positif mais correction non applicable a court terme (xstream), avec justification.
