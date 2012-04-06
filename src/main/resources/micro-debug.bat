@echo off
REM ###################
REM # Copyright (C) 2011-2012  Christian Roesch
REM #
REM # This file is part of micro-debug.
REM #
REM # micro-debug is free software: you can redistribute it and/or modify
REM # it under the terms of the GNU General Public License as published by
REM # the Free Software Foundation, either version 3 of the License, or
REM # (at your option) any later version.
REM #
REM # micro-debug is distributed in the hope that it will be useful,
REM # but WITHOUT ANY WARRANTY; without even the implied warranty of
REM # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
REM # GNU General Public License for more details.
REM #
REM # You should have received a copy of the GNU General Public License
REM # along with micro-debug.  If not, see <http://www.gnu.org/licenses/>.
REM ###################

REM # directory of this script file
set DIR=%~dp0

java -Djava.util.logging.config.file="%DIR%\config\logging.properties" ^
     -cp .;"%DIR%\config";"%DIR%\micro-debug-${version}.jar" com.github.croesch.micro_debug.MicroDebug %*
