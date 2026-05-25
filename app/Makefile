.PHONY: build

start-docker-app:
	@docker run --rm -p 5173:5173 hexletprojects/qa_auto_java_testing_kanban_board_project_ru_app

test:
	@gradlew clean test

checkstyle:
	@gradlew checkstyleMain checkstyleTest