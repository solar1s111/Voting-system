package com.votingsystem.springboot.util;

import com.votingsystem.springboot.HasId;
import com.votingsystem.springboot.model.MenuItem;
import com.votingsystem.springboot.repository.MenuItemRepository;
import com.votingsystem.springboot.util.exception.IllegalRequestDataException;
import com.votingsystem.springboot.util.exception.NotFoundException;

public class ValidationUtil {
    public static void checkNew(HasId bean) {
        if (!bean.isNew()) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must be new (id=null)");
        }
    }

    //  Conservative when you reply, but accept liberally (http://stackoverflow.com/a/32728226/548473)
    public static void assureIdConsistent(HasId bean, int id) {
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.id() != id) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must has id=" + id);
        }
    }

    public static void checkNotFoundWithId(boolean found, int id) {
        if (!found) {
            throw new NotFoundException("Not found entity with id=" + id);
        }
    }

    public static <T> T checkNotFoundWithId(T object, int id) {
        checkNotFoundWithId(object != null, id);
        return object;
    }

    public static void checkOwn(MenuItem menuItem, MenuItemRepository r, int restaurantId, int menuItemId) {
        MenuItem chooseMenuItem = r.findById(menuItemId)
                .filter(item -> item.getRestaurant().getId() == restaurantId)
                .orElse(null);
        if (!menuItem.isNew() && chooseMenuItem == null) {
            throw new IllegalRequestDataException("");
        }
    }
}