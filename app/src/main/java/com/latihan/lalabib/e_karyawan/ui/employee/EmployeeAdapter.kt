package com.latihan.lalabib.e_karyawan.ui.employee

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.latihan.lalabib.e_karyawan.data.EmployeeEntities
import com.latihan.lalabib.e_karyawan.databinding.ItemListBinding
import java.text.NumberFormat
import java.util.Locale

class EmployeeAdapter(private val onItemClick: (EmployeeEntities) -> Unit) :
    ListAdapter<EmployeeEntities, EmployeeAdapter.EmployeeViewHolder>(DIFFUTIL) {

    private object DIFFUTIL : DiffUtil.ItemCallback<EmployeeEntities>() {
        override fun areItemsTheSame(oldItem: EmployeeEntities, newItem: EmployeeEntities): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: EmployeeEntities, newItem: EmployeeEntities): Boolean {
            return oldItem.id == newItem.id
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EmployeeViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        val employees = getItem(position)
        holder.bind(employees)
    }

    class EmployeeViewHolder(
        private val binding: ItemListBinding,
        val onItemClick: (EmployeeEntities) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(employees: EmployeeEntities) {
            binding.tvName.text = employees.nama
            binding.tvAddress.text = employees.alamat
            binding.tvGender.text = employees.jenisKelamin
            binding.tvPosition.text = employees.jabatan
            binding.tvLengthOfWork.text = employees.lamaBekerja

            //func to change number format to rupiah
            val formatter = NumberFormat.getCurrencyInstance(Locale("in", "id"))
            val hasilFormat = formatter.format(employees.gajiPokok.toString().toDouble())
            binding.tvSalary.text = hasilFormat.substringBefore(",")

            itemView.setOnClickListener { onItemClick(employees) }
        }
    }
}