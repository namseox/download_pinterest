package com.kma.travel.ui.main

import android.content.Intent
import android.util.Log
import androidx.navigation.fragment.findNavController
import com.kma.travel.R
import com.kma.travel.databinding.FragmentMainBinding
import com.kma.travel.ui.base.AbsBaseFragment
import com.kma.travel.ui.home.ManageViewPagerAdapter
import com.kma.travel.utils.ToastUtils
import com.kma.travel.utils.Utils.hideKeyboard
import com.kma.travel.utils.Utils.shareApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : AbsBaseFragment<FragmentMainBinding>() {

    override fun getLayout(): Int {
        return R.layout.fragment_main
    }

    override fun initView() {
        binding.topAppBar.setNavigationOnClickListener {
            it.hideKeyboard()
            binding.drawerlayout.open()
        }

        /*binding.navView.setNavigationItemSelectedListener { menuItem ->
            // Handle menu item selected
            menuItem.isChecked = true
            binding.drawerlayout.close()
            true
        }*/

        /*setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(true)*/

        val sectionAdapter = ManageViewPagerAdapter(parentFragmentManager)
        binding.viepager.adapter = sectionAdapter
        binding.MainTab.setupWithViewPager(binding.viepager)

        /*binding.toggleButton.setOnClickListener {

            binding.drawerlayout.openDrawer(GravityCompat.START)
        }

        binding.toolbar.setNavigationOnClickListener {
            binding.drawerlayout.openDrawer(GravityCompat.START)
        }*/

        binding.navView.setNavigationItemSelectedListener { menuItem ->
            // set item as selected to persist highlight
            menuItem.isChecked = false
            // close drawer when item is tapped
            binding.drawerlayout.closeDrawers()

            when (menuItem.itemId) {


                R.id.nav_disclaimer -> {
                    val action =
                        MainFragmentDirections.actionMainFragmentToPolicyAndTermFragment22()
                            .setParam(3)
                    findNavController().navigate(action)
                }

                R.id.nav_terms_of_service -> {
                    val action =
                        MainFragmentDirections.actionMainFragmentToPolicyAndTermFragment22()
                            .setParam(1)
                    findNavController().navigate(action)
                }

                R.id.nav_policy -> {
                    val action =
                        MainFragmentDirections.actionMainFragmentToPolicyAndTermFragment22()
                            .setParam(2)
                    findNavController().navigate(action)
                }

                R.id.nav_how_to_use -> {
                    val action =
                        MainFragmentDirections.actionMainFragmentToPolicyAndTermFragment22()
                            .setParam(4)
                    findNavController().navigate(action)
                }

                R.id.nav_contact_us -> {
                    ToastUtils.getInstance(requireContext()).showToast("Contact Us")
                }

                R.id.nav_share -> {
                    val uriShareApp = requireActivity().shareApp()
                        val intent= Intent()
                        intent.action=Intent.ACTION_SEND
                        intent.putExtra(Intent.EXTRA_TEXT,uriShareApp)
                        intent.type="text/plain"
                        startActivity(Intent.createChooser(intent,"Share To:"))


                }

            }
            false
        }
    }

}