package pin.karasev.transportcompanyapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pin.karasev.transportcompanyapp.models.User

class UsersAdapter(var users: List<User>, val context: AdminActivity) : RecyclerView.Adapter<UsersAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val login: TextView = view.findViewById(R.id.user_login)
        val email: TextView = view.findViewById(R.id.user_email)
        val role: TextView = view.findViewById(R.id.user_role)
        val btnMakeAdmin: Button = view.findViewById(R.id.btn_make_admin)
        val btnMakeUser: Button = view.findViewById(R.id.btn_make_user)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_in_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return users.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.login.text = "Логин: " + users[position].login
        holder.email.text = "Email: " + users[position].email
        holder.role.text = "Роль: " + users[position].role

        holder.btnMakeAdmin.setOnClickListener {
            context.updateUserRole(users[position].login, "admin")
        }

        holder.btnMakeUser.setOnClickListener {
            context.updateUserRole(users[position].login, "user")
        }
    }
}