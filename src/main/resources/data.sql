INSERT INTO Permiso VALUES (1,'Academico', 'Categoria de permisos academicos', 1, 0, '/images/icon_school.gif');
INSERT INTO Permiso VALUES (2,'Enfermedad', 'Categoria de permisos por enfermedad', 1, 0, '/images/icon_disease.gif');
INSERT INTO Permiso VALUES (3,'Permiso academ por examen', 'Presentacion de examenes', 1,  1, '/images/icon_school_quiz.gif');
INSERT INTO Permiso VALUES (4,'Permiso por enfermedad', 'Consulta en Clinica', 1,  2, '/images/icon_check_health.gif');
INSERT INTO Tipo_Usuario_Permiso VALUES (1,1,4,1);
INSERT INTO Tipo_Usuario_Permiso VALUES (2,2,3,1);
INSERT INTO Tipo_Usuario_Permiso VALUES (3,2,4,1);
