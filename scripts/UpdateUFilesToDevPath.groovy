/**
 * The file-upload plugin is not very smart, it stores absolute pathes into DB. 
 * Need that script to convert them for the development environment.
 * 
 * TODO use pathes from Grails config
 */
import groovy.sql.Sql
sql = Sql.newInstance( 'jdbc:postgresql://localhost/flexygames', 'postgres', '', 'org.postgresql.Driver' )
sql.eachRow( 'select * from ufile' ) {ufile ->
	def newPath = null
	if (ufile.path.indexOf('/home/asas/upload/flexygames/logo/') != -1) {
		newPath = ufile.path.replace("/home/asas/upload/flexygames/logo/", "E:\\Workspace\\GGTS\\FlexyGames\\upload\\logo\\")
	}
	if (ufile.path.indexOf('/home/asas/upload/flexygames/avatar/') != -1) {
		newPath = ufile.path.replace("/home/asas/upload/flexygames/avatar/", "E:\\Workspace\\GGTS\\FlexyGames\\upload\\avatar\\")
	}
	if (newPath) {
		println "Ufile $ufile.id : changing ${ufile.path} to $newPath"
		if (newPath.indexOf("/") != -1) {
			newPath = newPath.replace("/", "\\")
		}
		sql.execute("update ufile set path = $newPath where id = ${ufile.id}")
	}
}