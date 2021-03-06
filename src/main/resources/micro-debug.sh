#!/bin/bash
###################
# Copyright (C) 2011-2012  Christian Roesch
#
# This file is part of micro-debug.
#
# micro-debug is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# micro-debug is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with micro-debug.  If not, see <http://www.gnu.org/licenses/>.
###################

# directory of this script file
DIR="$(dirname "${BASH_SOURCE[0]}")"

java -Djava.util.logging.config.file="$DIR/config/logging.properties" \
     -cp .:"$DIR/config":"$DIR/micro-debug-${version}.jar" com.github.croesch.micro_debug.MicroDebug $@
