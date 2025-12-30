# language: es
@E5 @Quality @Evidence @AzureDevOps
Característica: E5 - Calidad + Evidencia + Vínculo en Azure DevOps
  Como QA Lead
  Quiero aplicar estándares de calidad con evidencia rastreable
  Para asegurar gobernanza y trazabilidad en Azure DevOps

  Escenario: Aplicar checklist DoD (Definition of Done)
    Dado que tengo el checklist de Definition of Done
    Cuando aplico los estándares de calidad
    Entonces debería validar:
      | criterio                      |
      | Locators estables             |
      | Waits explícitas              |
      | Datasets versionados          |
      | 2 corridas sin fallos         |
      | Evidencia capturada           |

  Escenario: Capturar evidencia con nomenclatura estándar
    Dado que necesito evidencia rastreable
    Cuando genero capturas y logs
    Entonces la nomenclatura debería seguir el estándar:
      | tipo       | formato                                                          |
      | Screenshot | TestName_DatasetName_Step_Status_Timestamp.png                   |
      | Log        | TestName_DatasetName_Timestamp.log                               |

  Escenario: Definir campos mínimos para trazabilidad en AzDo
    Dado que necesito vincular tests a Azure DevOps
    Cuando registro un defecto
    Entonces debería incluir campos mínimos:
      | campo              | ejemplo       |
      | ID Requerimiento   | REQ-001       |
      | Ambiente           | QA            |
      | Dataset            | NEG_pwd       |
      | Versión            | 1.0.0         |
      | Work Item          | #12345        |

  Escenario: Clasificar fallas correctamente
    Dado que un test falló
    Cuando analizo la causa raíz
    Entonces debería clasificarse en:
      | tipo              | descripción                    |
      | Script Error      | Error en código del test       |
      | Application Defect| Bug real de la aplicación      |
      | Environment Issue | Problema de infraestructura    |
      | Test Data Issue   | Datos de prueba incorrectos    |

  Escenario: Generar reporte ejecutivo de valor
    Dado que completé la ejecución de pruebas
    Cuando genero el reporte ejecutivo
    Entonces debería incluir:
      | métrica            |
      | Total de pruebas   |
      | Tasa de éxito      |
      | Cobertura          |
      | Defectos por prioridad |
      | ROI                |
      | Estabilidad        |
      | Riesgos identificados |
