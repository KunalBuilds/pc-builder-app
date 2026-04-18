package com.mypcapp.data

import com.mypcapp.model.ComponentCategory
import com.mypcapp.model.PCComponent

/**
 * Local database of PC components with Indian pricing (INR).
 * compatibleBrand: "Intel" | "AMD" | "" (brand-agnostic)
 * drawableResName: key into res/drawable for GPU cards.
 */
object ComponentDatabase {

    /** Returns full list for a category, optionally filtered by brand. */
    fun getComponentsByCategory(
        category: ComponentCategory,
        brand: String? = null
    ): List<PCComponent> {
        val all = when (category) {
            ComponentCategory.MOTHERBOARD -> motherboards
            ComponentCategory.CPU        -> cpus
            ComponentCategory.GPU        -> gpus
            ComponentCategory.PSU        -> psus
            ComponentCategory.SSD        -> ssds
            ComponentCategory.RAM        -> rams
            ComponentCategory.CABINET    -> cabinets
            ComponentCategory.MONITOR    -> monitors
        }
        return if (brand.isNullOrBlank()) all
        else all.filter { it.compatibleBrand.equals(brand, ignoreCase = true) || it.compatibleBrand.isBlank() }
    }

    /** Top N by performance score — used for "Top Suggestions" section. */
    fun getTopSuggestions(
        category: ComponentCategory,
        brand: String? = null,
        count: Int = 5
    ): List<PCComponent> =
        getComponentsByCategory(category, brand)
            .sortedByDescending { it.performanceScore }
            .take(count)

    // ─── Motherboards ───────────────────────────────────────────────────────
    private val motherboards = listOf(
        PCComponent("mb1", "ASUS Prime B660M-K", "ASUS", 8500.0, ComponentCategory.MOTHERBOARD,
            "LGA1700 • Micro-ATX • 2 RAM Slots • Max 64GB",
            "https://amzn.in/mb-b660mk", "", "", 55, compatibleBrand = "Intel"),
        PCComponent("mb2", "MSI B550M PRO-VDH", "MSI", 7500.0, ComponentCategory.MOTHERBOARD,
            "AM4 • Micro-ATX • 4 RAM Slots • Max 128GB",
            "https://amzn.in/mb-b550m", "", "", 60, compatibleBrand = "AMD"),
        PCComponent("mb3", "Gigabyte B660M DS3H", "Gigabyte", 9000.0, ComponentCategory.MOTHERBOARD,
            "LGA1700 • Micro-ATX • 4 RAM Slots • Max 128GB",
            "https://amzn.in/mb-b660mds3h", "", "", 62, compatibleBrand = "Intel"),
        PCComponent("mb4", "ASUS ROG Strix B550-F", "ASUS", 18000.0, ComponentCategory.MOTHERBOARD,
            "AM4 • ATX • 4 RAM Slots • Max 128GB • WiFi",
            "https://amzn.in/mb-rogb550f", "", "", 82, compatibleBrand = "AMD"),
        PCComponent("mb5", "MSI MAG Z690 Tomahawk", "MSI", 22000.0, ComponentCategory.MOTHERBOARD,
            "LGA1700 • ATX • 4 RAM Slots • Max 128GB • WiFi 6E",
            "https://amzn.in/mb-z690tomahawk", "", "", 88, compatibleBrand = "Intel"),
        PCComponent("mb6", "Gigabyte X570 AORUS Elite", "Gigabyte", 20000.0, ComponentCategory.MOTHERBOARD,
            "AM4 • ATX • 4 RAM Slots • Max 128GB • PCIe 4.0",
            "https://amzn.in/mb-x570aorus", "", "", 85, compatibleBrand = "AMD"),
        PCComponent("mb7", "ASUS ProArt Z690-Creator", "ASUS", 35000.0, ComponentCategory.MOTHERBOARD,
            "LGA1700 • ATX • 4 RAM Slots • Thunderbolt 4 • DDR5",
            "https://amzn.in/mb-z690creator", "", "", 95, compatibleBrand = "Intel"),
        PCComponent("mb8", "MSI MEG X570 ACE", "MSI", 30000.0, ComponentCategory.MOTHERBOARD,
            "AM4 • ATX • 4 RAM Slots • WiFi 6 • PCIe 4.0 • RGB",
            "https://amzn.in/mb-x570ace", "", "", 92, compatibleBrand = "AMD")
    )

    // ─── CPUs ────────────────────────────────────────────────────────────────
    private val cpus = listOf(
        PCComponent("cpu1", "Intel Core i3-12100F", "Intel", 7500.0, ComponentCategory.CPU,
            "Cores: 4 • Threads: 8 • 3.3 GHz • LGA1700",
            "https://amzn.in/cpu-i3-12100f", "", "", 55, compatibleBrand = "Intel"),
        PCComponent("cpu2", "Intel Core i5-12400F", "Intel", 13500.0, ComponentCategory.CPU,
            "Cores: 6 • Threads: 12 • 2.5 GHz • LGA1700",
            "https://amzn.in/cpu-i5-12400f", "", "", 72, compatibleBrand = "Intel"),
        PCComponent("cpu3", "AMD Ryzen 5 5600X", "AMD", 14000.0, ComponentCategory.CPU,
            "Cores: 6 • Threads: 12 • 3.7 GHz • AM4",
            "https://amzn.in/cpu-r5-5600x", "", "", 75, compatibleBrand = "AMD"),
        PCComponent("cpu4", "Intel Core i7-12700F", "Intel", 23000.0, ComponentCategory.CPU,
            "Cores: 12 • Threads: 20 • 2.1 GHz • LGA1700",
            "https://amzn.in/cpu-i7-12700f", "", "", 88, compatibleBrand = "Intel"),
        PCComponent("cpu5", "AMD Ryzen 7 5800X", "AMD", 22000.0, ComponentCategory.CPU,
            "Cores: 8 • Threads: 16 • 3.8 GHz • AM4",
            "https://amzn.in/cpu-r7-5800x", "", "", 87, compatibleBrand = "AMD"),
        PCComponent("cpu6", "Intel Core i9-13900K", "Intel", 45000.0, ComponentCategory.CPU,
            "Cores: 24 • Threads: 32 • 3.0 GHz • LGA1700",
            "https://amzn.in/cpu-i9-13900k", "", "", 98, compatibleBrand = "Intel"),
        PCComponent("cpu7", "AMD Ryzen 9 7900X", "AMD", 40000.0, ComponentCategory.CPU,
            "Cores: 12 • Threads: 24 • 4.7 GHz • AM5",
            "https://amzn.in/cpu-r9-7900x", "", "", 96, compatibleBrand = "AMD"),
        PCComponent("cpu8", "AMD Ryzen 5 7600X", "AMD", 25000.0, ComponentCategory.CPU,
            "Cores: 6 • Threads: 12 • 4.7 GHz • AM5",
            "https://amzn.in/cpu-r5-7600x", "", "", 80, compatibleBrand = "AMD")
    )

    // ─── GPUs ─────────────────────────────────────────────────────────────────
    private val gpus = listOf(
        PCComponent("gpu1", "NVIDIA GTX 1650", "NVIDIA", 12000.0, ComponentCategory.GPU,
            "4GB GDDR6 • TDP: 75W • 896 CUDA Cores",
            "https://amzn.in/gpu-gtx1650", "", "", 45,
            drawableResName = "gpu_gtx_1650"),
        PCComponent("gpu2", "NVIDIA RTX 3060", "NVIDIA", 25000.0, ComponentCategory.GPU,
            "12GB GDDR6 • TDP: 170W • 3584 CUDA Cores",
            "https://amzn.in/gpu-rtx3060", "", "", 70,
            drawableResName = "gpu_rtx_3060"),
        PCComponent("gpu3", "AMD RX 6600 XT", "AMD", 22000.0, ComponentCategory.GPU,
            "8GB GDDR6 • TDP: 160W • 2048 Stream Procs",
            "https://amzn.in/gpu-rx6600xt", "", "", 68,
            drawableResName = "gpu_rx_6600xt"),
        PCComponent("gpu4", "NVIDIA RTX 3070", "NVIDIA", 38000.0, ComponentCategory.GPU,
            "8GB GDDR6 • TDP: 220W • 5888 CUDA Cores",
            "https://amzn.in/gpu-rtx3070", "", "", 82,
            drawableResName = "gpu_rtx_3070"),
        PCComponent("gpu5", "NVIDIA RTX 4070", "NVIDIA", 60000.0, ComponentCategory.GPU,
            "12GB GDDR6X • TDP: 200W • 5888 CUDA Cores",
            "https://amzn.in/gpu-rtx4070", "", "", 92,
            drawableResName = "gpu_rtx_4070"),
        PCComponent("gpu6", "AMD RX 7900 XTX", "AMD", 90000.0, ComponentCategory.GPU,
            "24GB GDDR6 • TDP: 355W • 6144 Stream Procs",
            "https://amzn.in/gpu-rx7900xtx", "", "", 97,
            drawableResName = "gpu_rx_7900xtx")
    )

    // ─── RAM ─────────────────────────────────────────────────────────────────
    private val rams = listOf(
        PCComponent("ram1", "Corsair 8GB DDR4 3200MHz", "Corsair", 2500.0, ComponentCategory.RAM,
            "8GB • DDR4 • 3200MHz • CL16",
            "https://amzn.in/ram-corsair8", "", "", 45),
        PCComponent("ram2", "G.Skill Ripjaws 16GB DDR4 3200MHz", "G.Skill", 4500.0, ComponentCategory.RAM,
            "16GB (2x8GB) • DDR4 • 3200MHz • CL16",
            "https://amzn.in/ram-gskill16", "", "", 70),
        PCComponent("ram3", "Kingston Fury 32GB DDR4 3600MHz", "Kingston", 8500.0, ComponentCategory.RAM,
            "32GB (2x16GB) • DDR4 • 3600MHz • CL18",
            "https://amzn.in/ram-kingston32", "", "", 85),
        PCComponent("ram4", "Corsair Vengeance 32GB DDR5 5200MHz", "Corsair", 12000.0, ComponentCategory.RAM,
            "32GB (2x16GB) • DDR5 • 5200MHz • CL40",
            "https://amzn.in/ram-corsair-ddr5", "", "", 92)
    )

    // ─── SSDs ────────────────────────────────────────────────────────────────
    private val ssds = listOf(
        PCComponent("ssd1", "Kingston A400 480GB SATA", "Kingston", 3000.0, ComponentCategory.SSD,
            "480GB • SATA III • Read: 500MB/s • Write: 450MB/s",
            "https://amzn.in/ssd-kingstona400", "", "", 45),
        PCComponent("ssd2", "WD Blue SN570 1TB NVMe", "Western Digital", 5500.0, ComponentCategory.SSD,
            "1TB • PCIe NVMe Gen3 • Read: 3500MB/s • Write: 3000MB/s",
            "https://amzn.in/ssd-wdsn570", "", "", 72),
        PCComponent("ssd3", "Samsung 970 EVO Plus 1TB", "Samsung", 8500.0, ComponentCategory.SSD,
            "1TB • PCIe NVMe Gen3 • Read: 3500MB/s • Write: 3300MB/s",
            "https://amzn.in/ssd-samsung970", "", "", 85),
        PCComponent("ssd4", "Samsung 990 Pro 2TB NVMe", "Samsung", 16000.0, ComponentCategory.SSD,
            "2TB • PCIe NVMe Gen4 • Read: 7450MB/s • Write: 6900MB/s",
            "https://amzn.in/ssd-samsung990pro", "", "", 97)
    )

    // ─── PSUs ────────────────────────────────────────────────────────────────
    private val psus = listOf(
        PCComponent("psu1", "Cooler Master MWE 450W", "Cooler Master", 3500.0, ComponentCategory.PSU,
            "450W • 80+ Bronze • Non-Modular",
            "https://amzn.in/psu-cm450", "", "", 50),
        PCComponent("psu2", "Corsair CV550 550W", "Corsair", 4500.0, ComponentCategory.PSU,
            "550W • 80+ Bronze • Non-Modular",
            "https://amzn.in/psu-corsaircv550", "", "", 62),
        PCComponent("psu3", "Seasonic Focus GX-650 650W", "Seasonic", 9000.0, ComponentCategory.PSU,
            "650W • 80+ Gold • Fully Modular",
            "https://amzn.in/psu-seasonicgx650", "", "", 85),
        PCComponent("psu4", "be quiet! Straight Power 750W", "be quiet!", 13000.0, ComponentCategory.PSU,
            "750W • 80+ Gold • Fully Modular",
            "https://amzn.in/psu-bequiet750", "", "", 92)
    )

    // ─── Cabinets ────────────────────────────────────────────────────────────
    private val cabinets = listOf(
        PCComponent("cab1", "Antec NX200 M mATX", "Antec", 2500.0, ComponentCategory.CABINET,
            "Micro-ATX • Tempered Glass • 1x120mm front",
            "https://amzn.in/cab-antecnx200", "", "", 50),
        PCComponent("cab2", "Deepcool MATREXX 55 ATX", "Deepcool", 4000.0, ComponentCategory.CABINET,
            "ATX • Tempered Glass • 3x120mm RGB front",
            "https://amzn.in/cab-matrexx55", "", "", 68),
        PCComponent("cab3", "NZXT H510 ATX Mid Tower", "NZXT", 8500.0, ComponentCategory.CABINET,
            "ATX • Tempered Glass • 2x120mm front",
            "https://amzn.in/cab-nzxth510", "", "", 82),
        PCComponent("cab4", "Lian Li Lancool III ATX", "Lian Li", 12000.0, ComponentCategory.CABINET,
            "ATX • Tempered Glass • 3x140mm ARGB front",
            "https://amzn.in/cab-lancool3", "", "", 93)
    )

    // ─── Monitors ────────────────────────────────────────────────────────────
    private val monitors = listOf(
        PCComponent("mon1", "LG 22MK430H 22\" FHD", "LG", 8000.0, ComponentCategory.MONITOR,
            "22 inch • 1920x1080 IPS • 75Hz",
            "https://amzn.in/mon-lg22", "", "", 50),
        PCComponent("mon2", "MSI G274F 27\" FHD 144Hz", "MSI", 16000.0, ComponentCategory.MONITOR,
            "27 inch • 1920x1080 IPS • 144Hz",
            "https://amzn.in/mon-msig274", "", "", 72),
        PCComponent("mon3", "Samsung 27\" QHD 165Hz Curved", "Samsung", 22000.0, ComponentCategory.MONITOR,
            "27 inch • 2560x1440 VA Curved • 165Hz",
            "https://amzn.in/mon-samsung27qhd", "", "", 85),
        PCComponent("mon4", "LG 27GP850-B 27\" QHD Nano IPS", "LG", 28000.0, ComponentCategory.MONITOR,
            "27 inch • 2560x1440 Nano IPS • 165Hz",
            "https://amzn.in/mon-lg27gp850", "", "", 94)
    )

    fun suggestBetterComponents(current: PCComponent, budget: Double): List<PCComponent> {
        val allInCategory = getComponentsByCategory(current.category)
        return allInCategory
            .filter { it.price <= budget && it.performanceScore > current.performanceScore }
            .sortedByDescending { it.performanceScore }
            .take(2)
    }

    fun getAffordableComponents(category: ComponentCategory, maxBudget: Double): List<PCComponent> {
        return getComponentsByCategory(category)
            .filter { it.price <= maxBudget }
            .sortedByDescending { it.performanceScore }
            .take(3)
    }
}
