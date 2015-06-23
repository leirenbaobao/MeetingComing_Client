package com.ctrlz.meetingcoming.launch;

public enum Duration {
	halfhour {
		@Override
		public int getMinute() {
			return 30;
		}

		@Override
		public String getName() {
			return "30分钟";
		}
	},
	threequarter {
		@Override
		public int getMinute() {
			return 45;
		}

		@Override
		public String getName() {
			return "45分钟";
		}
	},
	onehour {
		@Override
		public int getMinute() {
			return 60;
		}

		@Override
		public String getName() {
			return "1小时";
		}
	},
	fivequarter {
		@Override
		public int getMinute() {
			return 75;
		}

		@Override
		public String getName() {
			return "75分钟";
		}
	},
	onehalf {
		@Override
		public int getMinute() {
			return 90;
		}

		@Override
		public String getName() {
			return "1个半小时";
		}
	},
	twohour {
		@Override
		public int getMinute() {
			return 120;
		}

		@Override
		public String getName() {
			return "2小时";
		}
	},
	twohalf {
		@Override
		public int getMinute() {
			return 150;
		}

		@Override
		public String getName() {
			return "2个半小时";
		}
	},
	threehour {
		@Override
		public int getMinute() {
			return 180;
		}

		@Override
		public String getName() {
			return "3小时";
		}
	},
	fourhour {
		@Override
		public int getMinute() {
			return 240;
		}

		@Override
		public String getName() {
			return "4小时";
		}
	},
	fivehour {
		@Override
		public int getMinute() {
			return 300;
		}

		@Override
		public String getName() {
			return "5小时";
		}
	};
	/*oneday {
		@Override
		public int getMinute() {
			return 480;
		}

		@Override
		public String getName() {
			return "1天";
		}
	};*/

	/**
	 * 获得时长(单位:分)
	 * 
	 * @return minute
	 */
	public abstract int getMinute();

	/**
	 * 获得名称
	 * 
	 * @return name
	 */
	public abstract String getName();
}
