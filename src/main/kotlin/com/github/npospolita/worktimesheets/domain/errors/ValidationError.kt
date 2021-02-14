package com.github.npospolita.worktimesheets.domain.errors

import java.lang.RuntimeException

class ValidationError(message: String) : RuntimeException(message) {

}
